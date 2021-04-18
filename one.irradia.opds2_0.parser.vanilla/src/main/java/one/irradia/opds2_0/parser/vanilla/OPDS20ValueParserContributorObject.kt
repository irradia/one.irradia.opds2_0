package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRParserObjectSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Contributor
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Name
import java.net.URI

/**
 * An OPDS 2.0 author parser.
 */

class OPDS20ValueParserContributorObject(
  onReceive: (FRParserContextType, OPDS20Contributor) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserObject<OPDS20Contributor>(onReceive) {

  private lateinit var name: OPDS20Name
  private var identifier: URI? = null
  private var sortAs: String? = null
  private val links: MutableList<OPDS20Link> = mutableListOf()

  override fun onCompleted(context: FRParserContextType): FRParseResult<OPDS20Contributor> {
    return FRParseResult.succeed(OPDS20Contributor(
      name = this.name,
      identifier = this.identifier,
      sortAs = this.sortAs ?: this.name.name,
      links = this.links.toList()))
  }

  override fun schema(context: FRParserContextType): FRParserObjectSchema {
    val nameSchema =
      FRParserObjectFieldSchema(
        name = "name",
        parser = { OPDS20ValueParserName { _, name -> this.name = name } })

    val identifierSchema =
      FRParserObjectFieldSchema(
        name = "identifier",
        parser = { FRValueParsers.forURI { identifier -> this.identifier = identifier } },
        isOptional = true)

    val sortAsSchema =
      FRParserObjectFieldSchema(
        name = "sortAs",
        parser = { FRValueParsers.forString { sortAs -> this.sortAs = sortAs } },
        isOptional = true)

    val linkSchema =
      FRParserObjectFieldSchema(
        name = "links",
        parser = {
          FRValueParsers.forArrayMonomorphic({
            OPDS20ValueParserLink { _, link -> this.links.add(link) }
          })
        },
        isOptional = true)

    return FRParserObjectSchema(listOf(
      nameSchema,
      identifierSchema,
      sortAsSchema,
      linkSchema))
  }
}