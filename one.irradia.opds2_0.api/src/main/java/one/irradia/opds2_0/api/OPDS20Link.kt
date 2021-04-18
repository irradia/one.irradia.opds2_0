package one.irradia.opds2_0.api

import one.irradia.mime.api.MIMEType
import java.net.URI

/**
 * A generic OPDS 2.0 link.
 */

sealed class OPDS20Link : OPDS20ElementType {

  /**
   * The MIME type of the link content.
   */

  abstract val type: MIMEType?

  /**
   * The relations of the link.
   */

  abstract val relations: List<String>

  /**
   * Title of the linked resource
   */

  abstract val title: String?

  /**
   * Height of the linked resource in pixels
   */

  abstract val height: Int?

  /**
   * Width of the linked resource in pixels
   */

  abstract val width: Int?

  /**
   * Duration of the linked resource in seconds
   */

  abstract val duration: Double?

  /**
   * Bit rate of the linked resource in kilobits per second
   */

  abstract val bitrate: Double?

  /**
   * The link target as a URI, if the target is directly expressible as one
   */

  abstract val hrefURI: URI?

  /**
   * A non-templated, basic link.
   */

  data class OPDS20LinkBasic(
    val href: URI,
    override val type: MIMEType? = null,
    override val relations: List<String> = listOf(),
    override val title: String? = null,
    override val height: Int? = null,
    override val width: Int? = null,
    override val duration: Double? = null,
    override val bitrate: Double? = null
  ) : OPDS20Link() {
    override val hrefURI: URI
      get() = this.href
  }

  /**
   * A templated link.
   */

  data class OPDS20LinkTemplated(
    val href: String,
    override val type: MIMEType? = null,
    override val relations: List<String> = listOf(),
    override val title: String? = null,
    override val height: Int? = null,
    override val width: Int? = null,
    override val duration: Double? = null,
    override val bitrate: Double? = null
  ) : OPDS20Link() {
    override val hrefURI: URI?
      get() = null
  }
}
