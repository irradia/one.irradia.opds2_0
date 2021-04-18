package one.irradia.opds2_0.api

data class OPDS20Title(
  val title: String = "",
  val byLanguage: Map<String, String> = mapOf()): OPDS20ElementType {

  fun ofLanguageOrDefault(language: String): String =
    if (this.byLanguage.containsKey(language)) {
      this.byLanguage[language]!!
    } else {
      this.title
    }

}
