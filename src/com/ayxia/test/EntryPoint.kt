package com.ayxia.test

import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.StringReader


/**
 * Created by John on 4/26/2017.
 */
fun main(args: Array<String>) {
  while (true) {
    var line = readLine()!!
    try {
      val tokenizer = Tokenizer(BufferedInputStream(ByteArrayInputStream(line.toByteArray())))
      val tokens : MutableList<Token> = mutableListOf()
      tokenizer.tokenize(cb = {
        token->if (token.type != Token.Type.WHITESPACE) tokens.add(token)
      })

      println(Parser(tokens.toTypedArray()).eval())
    }
    catch (e: Exception) {
      println(e)
    }
  }
}
