package one.irradia.opds2_0.parser.extension.library_simplified.internal

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.library_simplified.api.OPDS20Catalog
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType
import one.irradia.opds2_0.parser.vanilla.OPDS20ValueParserLink
import one.irradia.opds2_0.parser.vanilla.OPDS20ValueParserMetadata

/**
 * An OPDS 2.0 catalog parser.
 */

class OPDS20CatalogValueParser(
  private val extensions: List<OPDS20ExtensionType>,
  onReceive: (FRParserContextType, OPDS20Catalog) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Catalog>(onReceive) {

  private var links: List<OPDS20Link> = listOf()
  private var images: List<OPDS20Link> = listOf()
  private lateinit var metadata: OPDS20Metadata

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

    val imagesSchema =
      FRParserObjectFieldSchema(
        name = "images",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserLink() },
            receiver = { images -> this.images = images }
          )
        },
        isOptional = true
      )

    return FRParserObjectSchema(listOf(
      linksSchema,
      imagesSchema,
      metadataSchema
    ))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Catalog> {
    return FRParseResult.succeed(OPDS20Catalog(
      links = this.links,
      images = this.images,
      metadata = this.metadata
    ))
  }
}
