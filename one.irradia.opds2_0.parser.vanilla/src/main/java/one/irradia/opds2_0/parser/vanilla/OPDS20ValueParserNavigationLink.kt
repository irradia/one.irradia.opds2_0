package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.mime.api.MIMEType
import one.irradia.opds2_0.api.OPDS20NavigationLink
import java.net.URI

/**
 * A parser for OPDS 2.0 navigation links.
 */

class OPDS20ValueParserNavigationLink(
  onReceive: (FRParserContextType, OPDS20NavigationLink) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20NavigationLink>(onReceive) {

  private lateinit var href: URI
  private lateinit var title: String
  private var type: MIMEType? = null
  private var relation: String? = null

  override fun schema(context: FRParserContextType): FRParserObjectSchema {
    val hrefSchema =
      FRParserObjectFieldSchema(
        name = "href",
        parser = { FRValueParsers.forURI { uri -> this.href = uri } })

    val titleSchema =
      FRParserObjectFieldSchema(
        name = "title",
        parser = { FRValueParsers.forString { title -> this.title = title } })

    val typeSchema =
      FRParserObjectFieldSchema(
        name = "type",
        parser = { FRValueParsers.forMIME { mime -> this.type = mime } },
        isOptional = true)

    val relSchema =
      FRParserObjectFieldSchema(
        name = "rel",
        parser = { FRValueParsers.forString { relation -> this.relation = relation } },
        isOptional = true)

    return FRParserObjectSchema(listOf(hrefSchema, titleSchema, typeSchema, relSchema))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20NavigationLink> =
    FRParseResult.succeed(
      OPDS20NavigationLink(
        href = this.href,
        type = this.type,
        title = this.title,
        relation = this.relation))
}