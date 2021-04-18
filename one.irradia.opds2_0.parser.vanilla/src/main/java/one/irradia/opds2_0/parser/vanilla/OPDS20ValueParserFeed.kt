package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseError
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20ExtensionElementType
import one.irradia.opds2_0.api.OPDS20Feed
import one.irradia.opds2_0.api.OPDS20Group
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.api.OPDS20Navigation
import one.irradia.opds2_0.api.OPDS20Publication

/**
 * An OPDS 2.0 feed parser.
 */

class OPDS20ValueParserFeed(
  private val extensions: OPDS20FeedParserExtensions,
  onReceive: (FRParserContextType, OPDS20Feed) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Feed>(onReceive) {

  private lateinit var links: List<OPDS20Link>
  private lateinit var metadata: OPDS20Metadata
  private var groups: List<OPDS20Group> = listOf()
  private var navigation: List<OPDS20Link> = listOf()
  private var publications: List<OPDS20Publication> = listOf()
  private val extensionElements = mutableListOf<OPDS20ExtensionElementType>()

  override fun schema(
    context: FRParserContextType
  ): FRParserObjectSchema {

    val extensionSchemas = mutableListOf<FRParserObjectFieldSchema<*>>()
    for (extension in this.extensions.feedRoleExtensions) {
      val extensionSchema = extension.createObjectFieldSchema { e ->
        this.extensionElements.add(e)
      }
      extensionSchemas.add(extensionSchema)
    }

    val metadataSchema =
      FRParserObjectFieldSchema(
        name = "metadata",
        parser = {
          OPDS20ValueParserMetadata { _, meta -> this.metadata = meta }
        }
      )

    val linksSchema =
      FRParserObjectFieldSchema(
        name = "links",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserLink() },
            receiver = { links -> this.links = links }
          )
        },
        isOptional = true
      )

    val groupsSchema =
      FRParserObjectFieldSchema(
        name = "groups",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserGroup() },
            receiver = { groups -> this.groups = groups }
          )
        },
        isOptional = true
      )

    val navigationSchema =
      FRParserObjectFieldSchema(
        name = "navigation",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserLink() },
            receiver = { links -> this.navigation = links }
          )
        },
        isOptional = true
      )

    val publicationsSchema =
      FRParserObjectFieldSchema(
        name = "publications",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserPublication() },
            receiver = { publications -> this.publications = publications }
          )
        },
        isOptional = true
      )

    val fields = mutableListOf<FRParserObjectFieldSchema<*>>()
    fields.add(groupsSchema)
    fields.add(linksSchema)
    fields.add(metadataSchema)
    fields.add(navigationSchema)
    fields.add(publicationsSchema)
    fields.addAll(extensionSchemas)
    return FRParserObjectSchema(fields.toList())
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Feed> {
    val errors = mutableListOf<FRParseError>()

    if (!this.links.any { link -> link.relations.contains("self") }) {
      errors.add(context.errorOf(
        "OPDS 2.0 feeds MUST contain a 'links' section with a 'self' link"))
    }

    val receivedNavigation = this.navigation
    val feedNavigation =
      if (receivedNavigation != null) {
        OPDS20Navigation(receivedNavigation.toList())
      } else {
        null
      }

    return FRParseResult.errorsOr(listOf(), errors) {
      FRParseResult.succeed(OPDS20Feed(
        groups = this.groups,
        links = this.links,
        metadata = this.metadata,
        navigation = feedNavigation,
        publications = this.publications,
        uri = context.documentURI,
        extensions = this.extensionElements.toList()
      ))
    }
  }
}
