package one.irradia.opds2_0.api

import java.net.URI

/**
 * A contributor.
 */

data class OPDS20Contributor(

  /**
   * The name of the contributor.
   */

  val name: OPDS20Name,

  /**
   * The identifier for the contributor.
   */

  val identifier: URI? = null,

  /**
   * The string used to sort the contributor name.
   */

  val sortAs: String = name.name,

  /**
   * The contributor links.
   */

  val links: List<OPDS20Link> = listOf()) : Comparable<OPDS20Contributor> {

  override fun compareTo(other: OPDS20Contributor): Int {
    return this.sortAs.compareTo(other.sortAs)
  }
}

