package one.irradia.opds2_0.api

/**
 * The OPDS 2.0 navigation section.
 */

data class OPDS20Navigation(

  /**
   * The set of navigation links.
   */

  val links: List<OPDS20Link>

) : OPDS20ElementType
