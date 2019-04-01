package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserScalarOrObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRValueParserType
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Title

/**
 * An OPDS 2.0 title parser.
 */

class OPDS20ValueParserTitle(
  onReceive: (FRParserContextType, OPDS20Title) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserScalarOrObject<OPDS20Title>(onReceive) {

  private val titles = mutableMapOf<String, String>()

  private fun mapToTitle(
    context: FRParserContextType,
    byLanguage: Map<String, String>): FRParseResult<OPDS20Title> {
    return if (byLanguage.isEmpty()) {
      context.failureOf("At least one title is required")
    } else {
      FRParseResult.succeed(OPDS20Title(byLanguage = byLanguage))
    }
  }

  override fun onObject(context: FRParserContextType): FRValueParserType<OPDS20Title> =
    FRValueParsers.forObjectMap({ key ->
      FRValueParsers.forString { value ->
        this.titles[key] = value
      }
    }).flatMap { m -> mapToTitle(context, m) }

  override fun onScalar(context: FRParserContextType): FRValueParserType<OPDS20Title> =
    FRValueParsers.forString().map { title -> OPDS20Title(title) }
}