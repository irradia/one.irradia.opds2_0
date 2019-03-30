package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseError
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.mime.api.MIMEType
import one.irradia.opds2_0.api.OPDS20Feed
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata
import java.net.URI

object OPDS20ValueParsers {

  class OPDS20ValueParserLink(
    onReceive: (FRParserContextType, OPDS20Link) -> Unit = FRValueParsers.ignoringReceiverWithContext())
    : FRAbstractParserObject<OPDS20Link>(onReceive) {

    private lateinit var href: URI
    private var type: MIMEType? = null
    private var relation: String? = null

    override fun schema(context: FRParserContextType): FRParserObjectSchema =
      FRParserObjectSchema(listOf(
        FRParserObjectFieldSchema(
          "href", { FRValueParsers.forURI { uri -> this.href = uri } }),
        FRParserObjectFieldSchema(
          "type", { FRValueParsers.forMIME { mime -> this.type = mime } }),
        FRParserObjectFieldSchema(
          "rel", { FRValueParsers.forString { relation -> this.relation = relation } })))

    override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Link> =
      FRParseResult.succeed(
        OPDS20Link(
          href = this.href,
          type = this.type,
          relation = this.relation))
  }

  class OPDS20ValueParserMetadata(
    onReceive: (FRParserContextType, OPDS20Metadata) -> Unit = FRValueParsers.ignoringReceiverWithContext())
    : FRAbstractParserObject<OPDS20Metadata>(onReceive) {

    private lateinit var title: String

    override fun schema(context: FRParserContextType): FRParserObjectSchema =
      FRParserObjectSchema(listOf(
        FRParserObjectFieldSchema(
          "title", { FRValueParsers.forString { title -> this.title = title } })))

    override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Metadata> =
      FRParseResult.succeed(OPDS20Metadata(title = this.title))
  }

  class OPDS20ValueParserFeed(
    onReceive: (FRParserContextType, OPDS20Feed) -> Unit = FRValueParsers.ignoringReceiverWithContext())
    : FRAbstractParserObject<OPDS20Feed>(onReceive) {

    override fun schema(context: FRParserContextType): FRParserObjectSchema =
      FRParserObjectSchema(listOf(
        FRParserObjectFieldSchema(
          "metadata", { OPDS20ValueParserMetadata { _, meta -> this.metadata = meta } }),
        FRParserObjectFieldSchema(
          "links", {
          FRValueParsers.forArrayMonomorphic(
            forEach = { OPDS20ValueParserLink() },
            receiver = { links -> this.links = links })
        })))

    private lateinit var metadata: OPDS20Metadata
    private lateinit var links: List<OPDS20Link>

    override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Feed> {
      val errors = mutableListOf<FRParseError>()

      if (!this.links.any { link -> link.relation == "self" }) {
        errors.add(context.errorOf(
          "OPDS 2.0 feeds MUST contain a 'links' section with a 'self' link"))
      }

      return if (errors.isEmpty()) {
        FRParseResult.succeed(OPDS20Feed(
          uri = context.documentURI,
          metadata = this.metadata,
          links = this.links))
      } else {
        FRParseResult.FRParseFailed(errors.toList())
      }
    }
  }

}
