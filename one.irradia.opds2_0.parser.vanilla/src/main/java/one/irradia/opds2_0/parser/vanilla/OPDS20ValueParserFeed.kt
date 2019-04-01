package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseError
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Feed
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.api.OPDS20Navigation
import one.irradia.opds2_0.api.OPDS20NavigationLink
import one.irradia.opds2_0.api.OPDS20Publication

/**
 * An OPDS 2.0 feed parser.
 */

class OPDS20ValueParserFeed(
  onReceive: (FRParserContextType, OPDS20Feed) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Feed>(onReceive) {

  private lateinit var metadata: OPDS20Metadata
  private lateinit var links: List<OPDS20Link>
  private var navigation: List<OPDS20NavigationLink> = listOf()
  private var publications: List<OPDS20Publication> = listOf()

  override fun schema(context: FRParserContextType): FRParserObjectSchema {
    val metadataSchema =
      FRParserObjectFieldSchema(
        name = "metadata",
        parser = { OPDS20ValueParserMetadata { _, meta -> this.metadata = meta } })

    val linksSchema =
      FRParserObjectFieldSchema(
        name = "links",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserLink() },
            receiver = { links -> this.links = links })
        })

    val navigationSchema =
      FRParserObjectFieldSchema(
        name = "navigation",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserNavigationLink() },
            receiver = { links -> this.navigation = links })
        },
        isOptional = true)

    val publicationsSchema =
      FRParserObjectFieldSchema(
        name = "publications",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserPublication() },
            receiver = { publications -> this.publications = publications })
        },
        isOptional = true)

    return FRParserObjectSchema(listOf(
      metadataSchema,
      linksSchema,
      publicationsSchema,
      navigationSchema))
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

    return FRParseResult.errorsOr(errors) {
      FRParseResult.succeed(OPDS20Feed(
        uri = context.documentURI,
        metadata = this.metadata,
        navigation = feedNavigation,
        links = this.links))
    }
  }
}