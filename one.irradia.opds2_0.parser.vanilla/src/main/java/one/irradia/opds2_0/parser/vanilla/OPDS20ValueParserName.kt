package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserScalarOrObject
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRValueParserType
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Name

/**
 * An OPDS 2.0 name parser.
 */

class OPDS20ValueParserName(
  onReceive: (FRParserContextType, OPDS20Name) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserScalarOrObject<OPDS20Name>(onReceive) {

  private val names = mutableMapOf<String, String>()

  private fun mapToName(
    context: FRParserContextType,
    byLanguage: Map<String, String>): FRParseResult<OPDS20Name> {
    return if (byLanguage.isEmpty()) {
      context.failureOf("At least one name is required")
    } else {
      FRParseResult.succeed(OPDS20Name(byLanguage = byLanguage))
    }
  }

  override fun onObject(context: FRParserContextType): FRValueParserType<OPDS20Name> =
    FRValueParsers.forObjectMap({ key ->
      FRValueParsers.forString { value ->
        this.names[key] = value
      }
    }).flatMap { m -> mapToName(context, m) }

  override fun onScalar(context: FRParserContextType): FRValueParserType<OPDS20Name> =
    FRValueParsers.forString().map { name -> OPDS20Name(name) }
}