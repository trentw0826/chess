package chess;

import java.util.Collection;
import java.util.HashSet;

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

    // For every possible move, peek into the enemy board and see if the king is in check
    // If so, remove that move from the possibleMoves array
    ChessGame game = new ChessGame(board, board.getPiece(position).getTeamColor());
    for (ChessMove move : possibleMoves) {
      Collection<ChessMove> possibleEnemyMoves = peekPossibleEnemyMoves(game, move);
    }
    return possibleMoves;
  }

  /**
   * For a given move on a chess board, return what the enemy moves would be.
   *
   * @param game   chess game to be peeked
   * @param move    possible move on the board
   * @return        set of all enemy moves
   */
  private static Collection<ChessMove> peekPossibleEnemyMoves(ChessGame game, ChessMove move) {

    ChessBoard board = game.getBoard();
    ChessGame.TeamColor teamTurn = game.getTeamTurn();

    Collection<ChessMove> possibleEnemyMoves = new HashSet<>();
    ChessPiece movingPiece = board.getPiece(move);

    if (movingPiece == null) {
      throw new IllegalArgumentException("Attempted to move null piece");
    }

    // Make the move on copied game and check for all possible moves
    ChessGame peekGame = new ChessGame(board, teamTurn);
    try {
      peekGame.makeMove(move);
    } catch (InvalidMoveException e) {
      throw new RuntimeException(e);
    }

    // Generate all enemy positions and add their possible moves
    Collection<ChessPosition> enemyPositions = peekGame.iterateForFriendlyPieces();
    for (ChessPosition enemyPosition : enemyPositions) {
      possibleEnemyMoves.addAll(PieceMovement.getPossibleMoves(peekGame.getBoard(), enemyPosition));
    }

    return possibleEnemyMoves;
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
    // TODO: Implement (only if necessary)
    return false;
  }

  /**
   * Returns if the team on the given board is in check;
   *
   * @param board     board to revise for check
   * @param teamColor team to revise for being in check
   * @return          if the team is in check
   */
  private boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
    // TODO: Implement
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
  private boolean isInStaleMate(ChessBoard board, ChessGame.TeamColor teamColor) {
    // TODO: Implement
    return false;
  }
}
