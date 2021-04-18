package one.irradia.opds2_0.api

import org.joda.time.Instant
import java.net.URI

/**
 * Metadata for an OPDS 2.0 feed.
 */

data class OPDS20Metadata(

  /**
   * The unique identifier for the publication.
   */

  val identifier: URI?,

  /**
   * The title of the publication.
   */

  val title: OPDS20Title,

  /**
   * The subtitle of the publication.
   */

  val subtitle: OPDS20Title?,

  /**
   * The time the publication was last modified.
   */

  val modified: Instant?,

  /**
   * The time the publication was published.
   */

  val published: Instant?,

  /**
   * The languages that apply to the publication.
   */

  val languages: List<String>,

  /**
   * The text value used to sort the publication.
   */

  val sortAs: String?,

  /**
   * The authors.
   */

  val author: List<OPDS20Contributor>): Comparable<OPDS20Metadata>, OPDS20ElementType {

  override fun compareTo(other: OPDS20Metadata): Int =
    (this.sortAs ?: this.title.title).compareTo(other.sortAs ?: other.title.title)

}
