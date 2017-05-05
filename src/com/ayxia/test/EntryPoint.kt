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
      println(Parser(tokenizer).eval())
    }
    catch (e: Exception) {
      println(e)
    }
  }
}
