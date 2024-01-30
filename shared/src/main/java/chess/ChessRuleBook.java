package chess;

import java.util.Collection;
import java.util.Iterator;

import static chess.ChessGame.TeamColor;

public interface ChessRuleBook {

  /**
   * Returns a collection of valid chess moves on a given board at a given position;
   * Takes all the valid moves for the piece and reduces them by those that would leave
   * the king in danger.
   *
   * @param board    given chess board
   * @param position position on the given chess board
   * @return collection of valid chess moves
   */
  static Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {

    Collection<ChessMove> possibleMoves = PieceMovement.getPossibleMoves(board, position);
    TeamColor turnColor = board.getPiece(position).getTeamColor();

    // For every possible move, see if the king would be in check
    Iterator<ChessMove> iterator = possibleMoves.iterator();
    while (iterator.hasNext()) {
      ChessMove move = iterator.next();
      ChessBoard boardAfterMove = new ChessBoard(board);

      try {
        boardAfterMove.makeMove(move);
      } catch (InvalidMoveException e) {
        throw new RuntimeException(e);
      }

      // Pass the theoretical board into isInCheck as if it were still (turnColor)'s turn
      if (isInCheck(boardAfterMove, turnColor)) {
        iterator.remove();  // Use iterator to safely remove the current element
      }
    }
    return possibleMoves;
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
//    // TODO: Implement (only if necessary)
//    return false;
//  }

  /**
   * Returns if the team on the given board is in check;
   *
   * @param board     board to revise for check
   * @param teamColor team to revise for being in check
   * @return          if the team is in check
   */
  private static boolean isInCheck(ChessBoard board, TeamColor teamColor) {

    TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

    // For each enemy position, check if any of its moves can take the white king
    Collection<ChessPosition> enemyPositions = board.iterateForFriendlyPieces(enemyColor);
    for (ChessPosition anEnemyPosition : enemyPositions) {
      Collection<ChessMove> possibleEnemyMoves = PieceMovement.getPossibleMoves(board, anEnemyPosition);
      for (ChessMove possibleEnemyMove : possibleEnemyMoves) {
        if (board.landsOnEnemy(possibleEnemyMove) && board.getCapturedPiece(possibleEnemyMove).getPieceType() == ChessPiece.PieceType.KING) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns if the team on the given board is in checkmate.
   *
   * @param board     board to revise for checkmate
   * @param teamColor team to revise for being in checkmate
   * @return          if said team is in checkmate
   */
  private boolean isInCheckMate(ChessBoard board, TeamColor teamColor) {
    // TODO: Implement
    return false;
  }

  /**
   * Returns if the team on the given board is in stalemate.
   *
   * @param board     board to revise for stalemate
   * @param teamColor team to revise for being in stalemate
   * @return          if said team is in stalemate
   */
  private boolean isInStaleMate(ChessBoard board, TeamColor teamColor) {
    // TODO: Implement
    return false;
  }
}
