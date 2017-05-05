package com.ayxia.test

/**
 * Created by John on 4/26/2017.
 */

class Parser(val tokenizer: Tokenizer) {
  val tokens: MutableList<Token> = mutableListOf()

  init {
    next()
  }

  fun eval() : Double =  parseExpr().eval()

  fun parseExpr() : Expr {
    return parseAdd()
  }

  fun parseAdd(): Expr {
    var e = parseSubtract()
    while (match(Token.Type.PLUS))
      e = BinaryOp(e, parseAdd(), '+')
    return e
  }

  fun parseSubtract(): Expr {
    var e = parseMultiply()
    while (match(Token.Type.MINUS))
      e = BinaryOp(e, parseSubtract(), '-')
    return e
  }

  fun parseMultiply(): Expr {
    var e = parseDivide()
    while (match(Token.Type.STAR))
      e = BinaryOp(e, parseMultiply(), '*')
    return e
  }

  fun parseDivide(): Expr {
    var e = parsePower()
    while (match(Token.Type.SLASH))
      e = BinaryOp(e, parseDivide(), '/')
    return e
  }

  fun parsePower(): Expr {
    var e = parseUnary()
    while (match(Token.Type.CARAT))
      e = BinaryOp(e, parsePower(), '^')
    return e
  }

  fun parseUnary(): Expr {
    if (match(Token.Type.PLUS, Token.Type.MINUS))
      return UnaryOp(prev().value.first(), parseUnary())
    return parsePrimary()
  }

  fun parsePrimary(): Expr {
    if (match(Token.Type.NUMBER))
      return Number(prev().value.toDouble())
    if (match(Token.Type.LPAREN)) {
      val p = parseExpr()
      next()
      return p
    }
    throw Exception("parse failure")
  }

  fun next() : Token {
    tokens.add(tokenizer.next())
    return tokens[tokens.size-1]
  }

  fun prev() = tokens[tokens.size-2]

  fun match(vararg t: Token.Type) : Boolean {
    val token = tokens[tokens.size-1].type
      t.forEach {
        if (token == it) {
          next()
          return true
        }
      }
    return false
  }

  abstract class Expr {
    abstract fun eval(): Double
  }
  open class Number(val value: Double) : Expr() {
    override fun eval(): Double = value
  }
  open class BinaryOp(val left: Expr, val right: Expr, val op: Char) : Expr() {
    override fun eval(): Double {
      when (op) {
        '+' -> return left.eval() + right.eval()
        '-' -> return left.eval() - right.eval()
        '*' -> return left.eval() * right.eval()
        '/' -> return left.eval() / right.eval()
        '^' -> return Math.pow(left.eval(), right.eval())
        else -> throw Exception("unexpected operator")
      }
    }
  }
  open class UnaryOp(val op: Char, val expr: Expr): Expr() {
    override fun eval() : Double =
        if (op == '-')  -expr.eval()
        else if (op=='+') expr.eval()
        else throw Exception()
  }


}

