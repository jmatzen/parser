package com.ayxia.test

/**
 * Created by John on 4/26/2017.
 */

data class Token(val type: Type, val value: String = "") {
  enum class Type {
    NUMBER,LPAREN,RPAREN,PLUS,MINUS,STAR,SLASH,WHITESPACE, CARAT, END
  }
}