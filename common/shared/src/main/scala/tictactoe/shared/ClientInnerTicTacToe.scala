package tictactoe.shared

import zio.json.JsonCodec
import zio.schema.{DeriveSchema, Schema}

case class ClientInnerTicTacToe(override val fields: List[Field] = List.fill(9)(EMPTY), override val phase: Phase = TurnOfX)
  extends AbstractInnerTicTacToe(fields, phase) 

object ClientInnerTicTacToe {
  implicit val schema: Schema[ClientInnerTicTacToe] = DeriveSchema.gen[ClientInnerTicTacToe]
  implicit val codec: JsonCodec[ClientInnerTicTacToe] =
    zio.schema.codec.JsonCodec.jsonCodec(schema)
}
