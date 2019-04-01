package one.irradia.opds2_0.api

/**
 * An OPDS 2.0 publication.
 */

data class OPDS20Publication(

  /**
   * Metadata for a publication.
   */

  val metadata: OPDS20Metadata,

  /**
   * Links for the publication.
   */

  val links: List<OPDS20Link> = listOf(),

  /**
   * The reading order for the publication.
   */

  val readingOrder: List<OPDS20Link> = listOf(),

  /**
   * The resources for the publication.
   */

  val resources: List<OPDS20Link> = listOf(),

  /**
   * The table of contents for the publication.
   */

  val tableOfContents: List<OPDS20Link> = listOf()): OPDS20ElementType
