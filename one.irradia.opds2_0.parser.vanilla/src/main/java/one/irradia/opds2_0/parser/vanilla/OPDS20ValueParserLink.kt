package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.mime.api.MIMEType
import one.irradia.opds2_0.api.OPDS20Link
import java.net.URI

/**
 * An OPDS 2.0 link parser.
 */

class OPDS20ValueParserLink(
  onReceive: (FRParserContextType, OPDS20Link) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Link>(onReceive) {

  private lateinit var href: URI
  private var type: MIMEType? = null
  private val relations = mutableListOf<String>()

  override fun schema(context: FRParserContextType): FRParserObjectSchema {
    val hrefSchema =
      FRParserObjectFieldSchema(
        name = "href",
        parser = { FRValueParsers.forURI { uri -> this.href = uri } })

    val typeSchema =
      FRParserObjectFieldSchema(
        name = "type",
        parser = { FRValueParsers.forMIME { mime -> this.type = mime } },
        isOptional = true)

    val relSchema =
      FRParserObjectFieldSchema(
        name = "rel",
        parser = {
          FRValueParsers.forArrayOrSingle({
            FRValueParsers.forString { relation -> this.relations.add(relation) }
          })
        },
        isOptional = true)

    return FRParserObjectSchema(listOf(hrefSchema, typeSchema, relSchema))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Link> =
    FRParseResult.succeed(
      OPDS20Link(
        href = this.href,
        type = this.type,
        relations = this.relations.toList()))
}