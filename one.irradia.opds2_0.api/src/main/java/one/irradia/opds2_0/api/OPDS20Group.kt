package one.irradia.opds2_0.api

/**
 * An OPDS 2.0 group.
 */

data class OPDS20Group(

  /**
   * The feed metadata.
   */

  val metadata: OPDS20Metadata,

  /**
   * The navigation section, if any.
   */

  val navigation: OPDS20Navigation?,

  /**
   * The publications, if any.
   */

  val publications: List<OPDS20Publication>,

  /**
   * The feed links
   */

  val links: List<OPDS20Link>

) : OPDS20ElementType

