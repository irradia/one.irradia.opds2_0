package one.irradia.opds2_0.parser.extension.spi

import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.opds2_0.api.OPDS20ExtensionElementType

/**
 * An extension for adding elements to metadata roles.
 */

interface OPDS20MetadataRoleExtensionType<T : OPDS20ExtensionElementType> {

  /**
   * @return A set of schemas for the extension
   */

  fun createCompositeFieldExtensionSchemas(
    extensions: List<OPDS20ExtensionType>
  ): ExtensionSchemas<T>

  /**
   * A set of object field schemas that will be evaluated for the current object, along with
   * a function [onCompletion] that must be called by the parent parser's
   * [one.irradia.fieldrush.api.FRParserObjectType.onCompleted] method to receive the completed
   * object.
   */

  data class ExtensionSchemas<T>(
    val objectFieldSchemas: List<FRParserObjectFieldSchema<*>>,
    val onCompletion: () -> T
  )
}
