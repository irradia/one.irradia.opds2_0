package one.irradia.opds2_0.library_simplified.api

import one.irradia.opds2_0.api.OPDS20ExtensionElementType
import org.joda.time.DateTime
import java.net.URI

/**
 * Metadata extensions for an OPDS 2.0 catalog.
 *
 * @see "https://github.com/NYPL-Simplified/Simplified/wiki/LibraryRegistryPublicAPI"
 */

data class OPDS20CatalogMetadata(

  /**
   * Used by Android clients (internally) to indicate if a library is in production.
   */

  val isProduction: Boolean,

  /**
   * Used by Android clients (internally) to indicate if a library should be automatically added
   * to the collection of active accounts on startup.
   */

  val isAutomatic: Boolean,

  /**
   * Library Simplified catalogs might serve an "adobe_vendor_id" property.
   */

  val adobeVendorId: String?,

  /**
   * Library Simplified catalogs use "updated" instead of "modified".
   *
   * @see "https://readium.org/webpub-manifest/context.jsonld"
   */

  val updated: DateTime?,

  /**
   * Library Simplified catalogs use "id" instead of "identifier".
   *
   * @see "https://readium.org/webpub-manifest/context.jsonld"
   */

  val id: URI?,

  /**
   * Library Simplified catalogs use a "location" property to express library service areas.
   *
   * @see "https://github.com/NYPL-Simplified/Simplified/wiki/LibraryRegistryPublicAPI#the-location-property"
   */

  val location: String?,

  /**
   * Library Simplified catalogs use a "distance" property to express the distance between the user and a library.
   *
   * @see "https://github.com/NYPL-Simplified/Simplified/wiki/LibraryRegistryPublicAPI#the-distance-property"
   */

  val distance: String?

) : OPDS20ExtensionElementType
