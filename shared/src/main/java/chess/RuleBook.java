package chess;

import java.util.Collection;

public interface RuleBook {
  Collection<ChessMove> validMoves = null;

  default Collection<ChessMove> getValidMoves() {
    return validMoves;
  }

  /**
   * Returns if a given ChessBoard object represents a valid chess board position.
   * A board's position is valid if: All positions should have exactly one king for each side.
   * No more than 8 pawns per side.
   * No pawns on the first or eighth ranks.
   * Both kings can't be in check
   * The player not on the move can't be in check.
   *
   * @param board board to be checked
   * @return      if board is valid
   */
  private boolean isBoardValid(ChessBoard board) {
    return false;
  }

  /**
   * Returns if the team on the given board is in check.
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
