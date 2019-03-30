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

    val result: T) : OPDS20ParseResult<T>()

  /**
   * Parsing failed.
   */

  data class OPDS20ParseFailed<T>(

    /**
     * The list of parse errors.
     */

    val errors: List<OPDS20ParseError>) : OPDS20ParseResult<T>()

  companion object {

    /**
     * Functor map.
     * If r == FRParseSucceeded(x), return FRParseSucceeded(f(x))
     * If r == FRParseFailed(y), return FRParseFailed(y)
     */

    fun <A, B> map(x: OPDS20ParseResult<A>, f: (A) -> B): OPDS20ParseResult<B> {
      return when (x) {
        is OPDS20ParseSucceeded -> OPDS20ParseSucceeded(f.invoke(x.result))
        is OPDS20ParseFailed -> OPDS20ParseFailed(x.errors)
      }
    }

    /**
     * Monadic bind.
     * If r == FRParseSucceeded(x), return f(r)
     * If r == FRParseFailed(y), return FRParseFailed(y)
     */

    fun <A, B> flatMap(x: OPDS20ParseResult<A>, f: (A) -> OPDS20ParseResult<B>): OPDS20ParseResult<B> {
      return when (x) {
        is OPDS20ParseSucceeded -> f.invoke(x.result)
        is OPDS20ParseFailed -> OPDS20ParseFailed(x.errors)
      }
    }

    /**
     * Construct a successful parse result.
     */

    fun <A> succeed(x: A): OPDS20ParseResult<A> {
      return OPDS20ParseSucceeded(x)
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
