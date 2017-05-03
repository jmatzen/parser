package com.ayxia.test

/**
 * Created by John on 4/26/2017.
 */

class Parser(val tokens: Array<Token>) {
  var index: Int = 0

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
    when (next().type) {
      Token.Type.NUMBER -> { return Number(prev().value.toDouble()) }
      Token.Type.LPAREN -> { val p = parseExpr(); next(); return p }
    }
    throw Exception("parse failure")
  }

  fun next() = if (index == tokens.size) Token(Token.Type.END) else tokens[index++]

  fun prev() = tokens[index-1]

  fun match(vararg t: Token.Type) : Boolean {
    if (index < tokens.size) {
      t.forEach {
        if (tokens[index].type == it) {
          next()
          return true
        }
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

