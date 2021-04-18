package one.irradia.opds2_0.parser.extension.spi

import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.opds2_0.api.OPDS20ExtensionElementType

/**
 * An extension for adding top-level roles to feeds.
 */

interface OPDS20FeedRoleExtensionType<T : OPDS20ExtensionElementType> {

  /**
   * The name of the extension
   */

  val name: String

  /**
   * The version of the extension
   */

  val version: String

  /**
   * @return A parser for the top-level object
   */

  fun createObjectFieldSchema(
    receiver: (T) -> Unit
  ): FRParserObjectFieldSchema<T>
}
