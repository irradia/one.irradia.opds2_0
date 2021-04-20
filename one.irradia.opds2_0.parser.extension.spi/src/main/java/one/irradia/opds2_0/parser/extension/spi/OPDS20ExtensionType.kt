package one.irradia.opds2_0.parser.extension.spi

/**
 * An OPDS 2.0 extension.
 */

interface OPDS20ExtensionType {

  /**
   * The name of the extension
   */

  val name: String

  /**
   * The version of the extension
   */

  val version: String

  /**
   * The declared feed role extensions.
   */

  fun feedRoleExtensions(): List<OPDS20FeedRoleExtensionType<*>>

  /**
   * The declared metadata role extensions.
   */

  fun metadataRoleExtension(): List<OPDS20MetadataRoleExtensionType<*>>

}
