package one.irradia.opds2_0.api

/**
 * The result of parsing a document.
 */

sealed class OPDS20ParseResult<T> {

  /**
   * Parsing succeeded.
   */

  data class OPDS20ParseSucceeded<T>(

    /**
     * The parsed value.
     */

    val result: T,

    /**
     * The list of parse warnings.
     */

    val warnings: List<OPDS20ParseWarning>

  ) : OPDS20ParseResult<T>()

  /**
   * Parsing failed.
   */

  data class OPDS20ParseFailed<T>(

    /**
     * The list of parse warnings.
     */

    val warnings: List<OPDS20ParseWarning>,

    /**
     * The list of parse errors.
     */

    val errors: List<OPDS20ParseError>

  ) : OPDS20ParseResult<T>()

  companion object {

    /**
     * Functor map.
     * If r == FRParseSucceeded(x), return FRParseSucceeded(f(x))
     * If r == FRParseFailed(y), return FRParseFailed(y)
     */

    fun <A, B> map(
      x: OPDS20ParseResult<A>,
      f: (A) -> B
    ): OPDS20ParseResult<B> {
      return when (x) {
        is OPDS20ParseSucceeded ->
          OPDS20ParseSucceeded(
            result = f.invoke(x.result),
            warnings = x.warnings
          )
        is OPDS20ParseFailed ->
          OPDS20ParseFailed(
            warnings = x.warnings,
            errors = x.errors
          )
      }
    }

    /**
     * Monadic bind.
     * If r == FRParseSucceeded(x), return f(r)
     * If r == FRParseFailed(y), return FRParseFailed(y)
     */

    fun <A, B> flatMap(
      x: OPDS20ParseResult<A>,
      f: (A) -> OPDS20ParseResult<B>
    ): OPDS20ParseResult<B> {
      return when (x) {
        is OPDS20ParseSucceeded ->
          when (val result = f.invoke(x.result)) {
            is OPDS20ParseFailed ->
              OPDS20ParseFailed(
                warnings = result.warnings.plus(x.warnings),
                errors = result.errors
              )
            is OPDS20ParseSucceeded ->
              OPDS20ParseSucceeded(
                warnings = result.warnings.plus(x.warnings),
                result = result.result
              )
          }
        is OPDS20ParseFailed ->
          OPDS20ParseFailed(
            warnings = x.warnings,
            errors = x.errors
          )
      }
    }

    /**
     * Construct a successful parse result.
     */

    fun <A> succeed(x: A): OPDS20ParseResult<A> {
      return OPDS20ParseSucceeded(x, warnings = listOf())
    }

    /**
     * Construct a successful parse result.
     */

    fun <A> succeed(
      warnings: List<OPDS20ParseWarning>,
      x: A
    ): OPDS20ParseResult<A> {
      return OPDS20ParseSucceeded(x, warnings)
    }
  }

  /**
   * Functor map.
   * If r == FRParseSucceeded(x), return FRParseSucceeded(f(x))
   * If r == FRParseFailed(y), return FRParseFailed(y)
   */

  fun <U> map(f: (T) -> U): OPDS20ParseResult<U> =
    Companion.map(this, f)

  /**
   * Monadic bind.
   * If r == FRParseSucceeded(x), return f(r)
   * If r == FRParseFailed(y), return FRParseFailed(y)
   */

  fun <U> flatMap(f: (T) -> OPDS20ParseResult<U>): OPDS20ParseResult<U> =
    Companion.flatMap(this, f)
}
