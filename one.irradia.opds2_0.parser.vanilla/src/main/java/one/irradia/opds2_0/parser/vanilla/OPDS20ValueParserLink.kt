package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParseWarning
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.mime.api.MIMEType
import one.irradia.opds2_0.api.OPDS20Link
import java.net.URI
import java.net.URISyntaxException

/**
 * An OPDS 2.0 link parser.
 */

class OPDS20ValueParserLink(
  onReceive: (FRParserContextType, OPDS20Link) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Link>(onReceive) {

  companion object {
    private val WHITESPACE = "\\s+".toRegex()
  }

  private val relations = mutableListOf<String>()
  private var href: String = ""
  private var templated: Boolean = false
  private var title: String? = null
  private var type: MIMEType? = null
  private val warnings = mutableListOf<FRParseWarning>()

  override fun schema(context: FRParserContextType): FRParserObjectSchema {
    val hrefSchema =
      FRParserObjectFieldSchema(
        name = "href",
        parser = { FRValueParsers.forString { uri -> this.href = uri } })

    val typeSchema =
      FRParserObjectFieldSchema(
        name = "type",
        parser = { FRValueParsers.forMIME { mime -> this.type = mime } },
        isOptional = true)

    val templatedSchema =
      FRParserObjectFieldSchema(
        name = "templated",
        parser = { FRValueParsers.forBoolean { templated -> this.templated = templated } },
        isOptional = true)

    val titleSchema =
      FRParserObjectFieldSchema(
        name = "title",
        parser = { FRValueParsers.forString { title -> this.title = title } },
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

    return FRParserObjectSchema(
      listOf(hrefSchema, typeSchema, titleSchema, relSchema, templatedSchema))
  }

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Link> {
    if (this.templated) {
      return FRParseResult.succeed(
        OPDS20Link.OPDS20LinkTemplated(
          href = this.href,
          type = this.type,
          title = this.title,
          relations = this.relations.toList()
        ))
    }

    var fixedHref = this.href.trim()
    return try {
      if (fixedHref.contains(' ')) {
        fixedHref = fixedHref.replace(WHITESPACE, "")
        context.warn(
          caller = OPDS20ValueParserLink::class.java,
          message = "Fixed a broken URI: ${this.href}",
        )
      }

      val hrefUri = URI(fixedHref)
      FRParseResult.succeed(
        OPDS20Link.OPDS20LinkBasic(
          href = hrefUri,
          type = this.type,
          title = this.title,
          relations = this.relations.toList()
        ))
    } catch (e: URISyntaxException) {
      context.failureOf("Unable to parse URI: ${this.href}", e)
    }
  }
}
