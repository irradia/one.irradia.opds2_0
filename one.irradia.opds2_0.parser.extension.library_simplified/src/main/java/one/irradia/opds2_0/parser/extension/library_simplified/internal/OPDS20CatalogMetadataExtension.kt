package one.irradia.opds2_0.parser.extension.library_simplified.internal

import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.library_simplified.api.OPDS20CatalogMetadata
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType
import one.irradia.opds2_0.parser.extension.spi.OPDS20MetadataRoleExtensionType
import org.joda.time.DateTime
import java.net.URI

/**
 * An extension that allows for adding extra fields to a "metadata" role.
 */

class OPDS20CatalogMetadataExtension : OPDS20MetadataRoleExtensionType<OPDS20CatalogMetadata> {

  private var distance: String? = null
  private var id: URI? = null
  private var isAutomatic: Boolean = false
  private var isProduction: Boolean = false
  private var areaServed: String? = null
  private var updated: DateTime? = null

  override fun createCompositeFieldExtensionSchemas(
    extensions: List<OPDS20ExtensionType>
  ): OPDS20MetadataRoleExtensionType.ExtensionSchemas<OPDS20CatalogMetadata> {

    val isAutomaticSchema =
      FRParserObjectFieldSchema(
        name = "isAutomatic",
        parser = {
          FRValueParsers.forBoolean { this.isAutomatic = it }
        },
        isOptional = true
      )

    val idSchema =
      FRParserObjectFieldSchema(
        name = "id",
        parser = {
          FRValueParsers.forURI { this.id = it }
        },
        isOptional = true
      )

    val updatedSchema =
      FRParserObjectFieldSchema(
        name = "updated",
        parser = { FRValueParsers.forDateTimeUTC { updated -> this.updated = updated } },
        isOptional = true
      )

    val isProductionSchema =
      FRParserObjectFieldSchema(
        name = "isProduction",
        parser = {
          FRValueParsers.forBoolean { this.isProduction = it }
        },
        isOptional = true
      )

    val locationSchema =
      FRParserObjectFieldSchema(
        name = "schema:areaServed",
        parser = {
          FRValueParsers.forString { this.areaServed = it }
        },
        isOptional = true
      )

    val distanceSchema =
      FRParserObjectFieldSchema(
        name = "schema:distance",
        parser = {
          FRValueParsers.forString { this.distance = it }
        },
        isOptional = true
      )

    return OPDS20MetadataRoleExtensionType.ExtensionSchemas(
      objectFieldSchemas = listOf(
        distanceSchema,
        idSchema,
        isAutomaticSchema,
        isProductionSchema,
        locationSchema,
        updatedSchema,
      ),
      onCompletion = {
        OPDS20CatalogMetadata(
          distance = this.distance,
          id = this.id,
          isAutomatic = this.isAutomatic,
          isProduction = this.isProduction,
          areaServed = this.areaServed,
          updated = this.updated,
        )
      }
    )
  }
}
