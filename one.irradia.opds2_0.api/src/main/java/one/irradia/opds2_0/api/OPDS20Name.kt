package one.irradia.opds2_0.api

data class OPDS20Name(
  val name: String = "",
  val byLanguage: Map<String, String> = mapOf()): OPDS20ElementType {

  fun ofLanguageOrDefault(language: String): String =
    if (this.byLanguage.containsKey(language)) {
      this.byLanguage[language]!!
    } else {
      this.name
    }

}
