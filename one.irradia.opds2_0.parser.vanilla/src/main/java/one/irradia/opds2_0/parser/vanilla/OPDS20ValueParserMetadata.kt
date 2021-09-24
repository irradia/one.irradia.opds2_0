package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Contributor
import one.irradia.opds2_0.api.OPDS20ExtensionElementType
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.api.OPDS20Title
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import java.math.BigInteger
import java.net.URI

/**
 * An OPDS 2.0 metadata section parser.
 */

class OPDS20ValueParserMetadata(
  private val extensions: List<OPDS20ExtensionType>,
  onReceive: (FRParserContextType, OPDS20Metadata) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Metadata>(onReceive) {

  private lateinit var title: OPDS20Title
  private val authors: MutableList<OPDS20Contributor> = mutableListOf()
  private var identifier: URI? = null
  private var subtitle: OPDS20Title? = null
  private var modified: DateTime? = null
  private var published: DateTime? = null
  private val languages: MutableList<String> = mutableListOf()
  private var sortAs: String? = null
  private var description: String = ""
  private var duration: BigInteger = BigInteger.ZERO
  private val extensionCompletions = mutableListOf<() -> OPDS20ExtensionElementType>()

  override fun schema(context: FRParserContextType): FRParserObjectSchema {

    /*
     * Load any extra field schemas.
     */

    val extraFieldSchemas = mutableListOf<FRParserObjectFieldSchema<*>>()
    for (extension in this.extensions) {
      for (metadata in extension.metadataRoleExtension()) {
        val extensionSchemas = metadata.createCompositeFieldExtensionSchemas(this.extensions)
        extraFieldSchemas.addAll(extensionSchemas.objectFieldSchemas)
        this.extensionCompletions.add(extensionSchemas.onCompletion)
      }
    }

    /*
     * Declare the core metadata field schemas.
     */

    val identifierSchema =
      FRParserObjectFieldSchema(
        name = "identifier",
        parser = { FRValueParsers.forURI { identifier -> this.identifier = identifier } },
        isOptional = true)

    val titleSchema =
      FRParserObjectFieldSchema(
        name = "title",
        parser = { OPDS20ValueParserTitle { _, title -> this.title = title } })

    val subtitleSchema =
      FRParserObjectFieldSchema(
        name = "subtitle",
        parser = { OPDS20ValueParserTitle { _, subtitle -> this.subtitle = subtitle } },
        isOptional = true)

    val modifiedSchema =
      FRParserObjectFieldSchema(
        name = "modified",
        parser = { FRValueParsers.forDateTimeUTC { modified -> this.modified = modified } },
        isOptional = true)

    val publishedSchema =
      FRParserObjectFieldSchema(
        name = "published",
        parser = { FRValueParsers.forDateTimeUTC { published -> this.published = published } },
        isOptional = true)

    val languageSchema =
      FRParserObjectFieldSchema(
        name = "language",
        parser = {
          FRValueParsers.forArrayOrSingle(
            forItem = { FRValueParsers.forString() },
            receiver = { values -> this.languages.add(values) })
        },
        isOptional = true)

    val sortAsSchema =
      FRParserObjectFieldSchema(
        name = "sortAs",
        parser = { FRValueParsers.forString { sortAs -> this.sortAs = sortAs } },
        isOptional = true)

    val authorSchema =
      FRParserObjectFieldSchema(
        name = "author",
        parser = { OPDS20ValueParserContributor { _, authors -> this.authors.addAll(authors) } },
        isOptional = true)

    val descriptionSchema =
      FRParserObjectFieldSchema(
        name = "description",
        parser = { FRValueParsers.forString { description -> this.description = description } },
        isOptional = true)

    val durationSchema =
      FRParserObjectFieldSchema(
        name = "duration",
        parser = { FRValueParsers.forInteger { duration -> this.duration = duration } },
        isOptional = true)

    val fields = mutableListOf<FRParserObjectFieldSchema<*>>()
    fields.add(authorSchema)
    fields.add(descriptionSchema)
    fields.add(durationSchema)
    fields.add(identifierSchema)
    fields.add(languageSchema)
    fields.add(modifiedSchema)
    fields.add(publishedSchema)
    fields.add(sortAsSchema)
    fields.add(subtitleSchema)
    fields.add(titleSchema)
    fields.addAll(extraFieldSchemas)
    return FRParserObjectSchema(fields.toList())
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Metadata> {
    val extensionValues = mutableListOf<OPDS20ExtensionElementType>()
    for (completion in this.extensionCompletions) {
      extensionValues.add(completion.invoke())
    }

    return FRParseResult.succeed(OPDS20Metadata(
      title = this.title,
      identifier = this.identifier,
      subtitle = this.subtitle,
      description = this.description,
      modified = this.modified,
      published = this.published,
      languages = this.languages.toList(),
      sortAs = this.sortAs ?: this.title.title,
      author = this.authors.toList(),
      extensions = extensionValues.toList()
    ))
  }
}
