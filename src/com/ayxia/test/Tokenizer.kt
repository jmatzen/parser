package com.ayxia.test

import java.io.InputStream
import java.util.stream.Stream

/**
 * Created by John on 5/2/2017.
 */
class Tokenizer(val s: InputStream) {
  enum class Action {
    NONE,
    POP,
  }

  class State(val r: String, val type: Token.Type, val state: Array<State>? = null, val action: Action = Action.NONE) {
    val regex = Regex(r)
  }

  val table : Array<State> = arrayOf(
      State("\\s+", Token.Type.WHITESPACE),
      State("\\(", Token.Type.LPAREN),
      State("\\)", Token.Type.RPAREN),
      State("""\d+((\.)?\d*)?""", Token.Type.NUMBER),
      State("\\+", Token.Type.PLUS),
      State("\\-", Token.Type.MINUS),
      State("\\*", Token.Type.STAR),
      State("\\^", Token.Type.CARAT),
      State("\\/", Token.Type.SLASH)

//      State("struct", Token.Type.STRUCT),
//      State("interface", Token.Type.INTERFACE),
//      State("package", Token.Type.PACKAGE),
//      State("[A-Za-z_][A-Za-z_0-9$]*", Token.Type.IDENTIFIER),
//      State("//.*$", Token.Type.COMMENT),
//      State("/\\*", Token.Type.COMMENT,
//          arrayOf(State("\\*/", Token.Type.COMMENT, action = Action.POP))),
//      State("\"", Token.Type.STRING, arrayOf(
//          State("[^\"]+", Token.Type.STRING),
//          State("\"", Token.Type.STRING, action = Action.POP),
//          State("""\\.""", Token.Type.STRESCAPE))
//      )
  )

  fun tokenize(state: Array<State> = table, str:String = s.reader().readText(), begin_:Int = 0, end: Int = 1, cb: (Token)->Unit) {
    var begin = begin_
    var end_ = end;
    var bestMatch : Token.Type = Token.Type.WHITESPACE
    loop@ while (end_ <= str.length) {
      val sub = str.substring(begin, end_)
      for(s in state) {
        if (s.regex.matches(sub)) {
          ++end_
          bestMatch = s.type
          continue@loop
        }
      }
      val tokenValue = str.substring(begin, end_-1)
      cb(Token(bestMatch, tokenValue))
      begin = end_-1
    }
    cb(Token(bestMatch, str.substring(begin, end_-1)))

  }
}