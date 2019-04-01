package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Contributor
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.api.OPDS20Title
import org.joda.time.Instant
import java.math.BigInteger
import java.net.URI

/**
 * An OPDS 2.0 metadata section parser.
 */

class OPDS20ValueParserMetadata(
  onReceive: (FRParserContextType, OPDS20Metadata) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Metadata>(onReceive) {

  private lateinit var title: OPDS20Title
  private val authors: MutableList<OPDS20Contributor> = mutableListOf()
  private var identifier: URI? = null
  private var subtitle: OPDS20Title? = null
  private var modified: Instant? = null
  private var published: Instant? = null
  private val languages: MutableList<String> = mutableListOf()
  private var sortAs: String? = null
  private var description: String = ""
  private var duration: BigInteger = BigInteger.ZERO

  override fun schema(context: FRParserContextType): FRParserObjectSchema {

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
        parser = { FRValueParsers.forTimestamp { modified -> this.modified = modified } },
        isOptional = true)

    val publishedSchema =
      FRParserObjectFieldSchema(
        name = "published",
        parser = { FRValueParsers.forTimestamp { published -> this.published = published } },
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

    return FRParserObjectSchema(listOf(
      identifierSchema,
      titleSchema,
      subtitleSchema,
      modifiedSchema,
      publishedSchema,
      languageSchema,
      sortAsSchema,
      authorSchema,
      descriptionSchema,
      durationSchema))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Metadata> =
    FRParseResult.succeed(OPDS20Metadata(
      title = this.title,
      identifier = this.identifier,
      subtitle = this.subtitle,
      modified = this.modified,
      published = this.published,
      languages = this.languages.toList(),
      sortAs = this.sortAs ?: this.title.title,
      author = this.authors.toList()))
}