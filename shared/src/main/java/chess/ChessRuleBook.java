package chess;

import java.util.Collection;
import java.util.Collections;

public interface ChessRuleBook {

  /**
   * Returns a collection of valid chess moves on a given board at a given position.
   *
   * @param board     given chess board
   * @param position  position on the given chess board
   * @return          collection of valid chess moves
   */
  static Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {

    Collection<ChessMove> validMoves = PieceMovement.getPossibleMoves(board, position);
    /*
    Reductions to all possible moves:
      - No move can be made that would leave the king in check
      - If the king is already in check, only moves that take the king out of check are valid
        (Moving king out of check, blocking with other piece, taking the attacking piece)
     */

    return validMoves;
  }

  /**
   * For a given move on a chess board, return what the enemy moves would be.
   *
   * @param board   chess board to be peeked
   * @param move    possible move on the board
   * @return        set of all enemy moves
   */
  private Collection<ChessMove> peekEnemyMoves(ChessBoard board, ChessMove move) {

    ChessBoard peekBoard;
    try {
      peekBoard = board.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }

//     TODO: iterate over the peekBoard to a collection of possible valid moves
//     Collection<ChessMoves> possibleEnemyMoves
    return Collections.emptySet();
  }


//  /**
//   * Returns if a given ChessBoard object represents a valid chess board position.
//   * A board's position is valid if: All positions should have exactly one king for each side.
//   * No more than 8 pawns per side.
//   * No pawns on the first or eighth ranks.
//   * Both kings can't be in check
//   * The player not on the move can't be in check.
//   *
//   * @param board board to be checked
//   * @return      if board is valid
//   */
//  private boolean isBoardValid(ChessBoard board) {
//    return false;
//  }

  /**
   * Returns if the team on the given board is in check;
   *
   * @param board     board to revise for check
   * @param teamColor team to revise for being in check
   * @return          if the team is in check
   */
  private boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
    return false;
  }

  /**
   * Returns if the team on the given board is in checkmate.
   *
   * @param board     board to revise for checkmate
   * @param teamColor team to revise for being in checkmate
   * @return          if said team is in checkmate
   */
  private boolean isInCheckMate(ChessBoard board, ChessGame.TeamColor teamColor) {
    return false;
  }

  /**
   * Returns if the team on the given board is in stalemate.
   *
   * @param board     board to revise for stalemate
   * @param teamColor team to revise for being in stalemate
   * @return          if said team is in stalemate
   */
  private boolean isInStaleMate(ChessBoard board, ChessGame.TeamColor teamColor) {
    return false;
  }
}
