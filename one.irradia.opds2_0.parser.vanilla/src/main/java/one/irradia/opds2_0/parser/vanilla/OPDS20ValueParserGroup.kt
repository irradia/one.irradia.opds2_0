package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseError
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Group
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.api.OPDS20Navigation
import one.irradia.opds2_0.api.OPDS20Publication
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType

/**
 * An OPDS 2.0 group parser.
 */

class OPDS20ValueParserGroup(
  private val extensions: List<OPDS20ExtensionType>,
  onReceive: (FRParserContextType, OPDS20Group) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Group>(onReceive) {

  private lateinit var metadata: OPDS20Metadata
  private var links: List<OPDS20Link> = listOf()
  private var navigation: List<OPDS20Link> = listOf()
  private var publications: List<OPDS20Publication> = listOf()

  override fun schema(context: FRParserContextType): FRParserObjectSchema {
    val metadataSchema =
      FRParserObjectFieldSchema(
        name = "metadata",
        parser = {
          OPDS20ValueParserMetadata(this.extensions) { _, meta -> this.metadata = meta }
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
            forEach = { OPDS20ValueParserPublication(this.extensions) },
            receiver = { publications -> this.publications = publications }
          )
        },
        isOptional = true
      )

    return FRParserObjectSchema(listOf(
      metadataSchema,
      linksSchema,
      publicationsSchema,
      navigationSchema
    ))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Group> {
    val errors = mutableListOf<FRParseError>()

    val receivedNavigation = this.navigation
    val feedNavigation =
      if (receivedNavigation != null) {
        OPDS20Navigation(receivedNavigation.toList())
      } else {
        null
      }

    return FRParseResult.errorsOr(listOf(), errors) {
      FRParseResult.succeed(OPDS20Group(
        metadata = this.metadata,
        navigation = feedNavigation,
        publications = this.publications,
        links = this.links))
    }
  }
}
