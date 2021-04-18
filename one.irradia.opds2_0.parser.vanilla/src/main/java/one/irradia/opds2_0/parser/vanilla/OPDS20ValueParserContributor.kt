package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRAbstractParserScalarArrayOrObject
import one.irradia.fieldrush.api.FRParserContextType
import one.irradia.fieldrush.api.FRValueParserType
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.api.OPDS20Contributor

/**
 * An OPDS 2.0 contributor parser.
 */

class OPDS20ValueParserContributor(
  onReceive: (FRParserContextType, List<OPDS20Contributor>) -> Unit = FRValueParsers.ignoringReceiverWithContext())
  : FRAbstractParserScalarArrayOrObject<OPDS20Contributor>(onReceive) {

  override fun onArray(context: FRParserContextType): FRValueParserType<List<OPDS20Contributor>> =
    FRValueParsers.forArrayMonomorphic({ OPDS20ValueParserName().map { name -> OPDS20Contributor(name) } })

  override fun onObject(context: FRParserContextType) =
    OPDS20ValueParserContributorObject()

  override fun onScalar(context: FRParserContextType): FRValueParserType<OPDS20Contributor> =
    OPDS20ValueParserName().map { name -> OPDS20Contributor(name) }
}