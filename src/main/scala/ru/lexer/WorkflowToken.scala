package ru.lexer
import scala.util.parsing.combinator._

sealed trait WorkflowToken extends RegexParsers{

  override def skipWhitespace: Boolean = true
  override val whiteSpace =  "[ \t\r\f]+".r

  case class IDENTIFIER(str: String) extends WorkflowToken

  case class LITERAL(str: String) extends WorkflowToken

  case class INDENTATION(spaces: Int) extends WorkflowToken

  case object EXIT extends WorkflowToken

  case object READINPUT extends WorkflowToken

  case object CALLSERVICE extends WorkflowToken

  case object SWITCH extends WorkflowToken

  case object OTHERWISE extends WorkflowToken

  case object COLON extends WorkflowToken

  case object ARROW extends WorkflowToken

  case object EQUALS extends WorkflowToken

  case object COMMA extends WorkflowToken

  case object INDENT extends WorkflowToken

  case object DEDENT extends WorkflowToken
}