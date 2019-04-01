package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata
import one.irradia.opds2_0.api.OPDS20Publication

/**
 * An OPDS 2.0 publication section parser.
 */

class OPDS20ValueParserPublication(
  onReceive: (FRParserContextType, OPDS20Publication) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Publication>(onReceive) {

  private lateinit var metadata: OPDS20Metadata
  private var links: List<OPDS20Link> = listOf()
  private var readingOrder: List<OPDS20Link> = listOf()

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

    val readingOrderSchema =
      FRParserObjectFieldSchema(
        name = "readingOrder",
        parser = {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserLink() },
            receiver = { links -> this.readingOrder = links })
        },
        isOptional = true)

    return FRParserObjectSchema(listOf(
      metadataSchema,
      linksSchema,
      readingOrderSchema
    ))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Publication> =
    FRParseResult.succeed(OPDS20Publication(
      metadata = this.metadata,
      links = this.links,
      readingOrder = this.readingOrder
    ))
}