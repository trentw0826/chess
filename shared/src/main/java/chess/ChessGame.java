package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import static chess.ChessConstants.BOARD_SIZE;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    /**
     * Constructs a ChessGame object
     */
    public ChessGame() {
      board = new ChessBoard();
      teamTurn = TeamColor.WHITE;
    }

  public ChessGame(ChessBoard board, TeamColor teamTurn) {
    try {
      this.board = board.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
    this.teamTurn = teamTurn;
  }


    /**
     * Overwritten equals method
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }


    /**
     * Overwritten hashCode method
     */
    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    /**
     * Overwritten toString method
     */
    @Override
    public String toString() {
        return board.toString();
    }


    /**
     * @return getter for teamTurn
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }


    /**
     * Setter for teamTurn
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }


  /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param position the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition position) {
        return ChessRuleBook.getValidMoves(board, position);
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to update 'board' attribute with
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
      // TODO: updated board.validateMove() to move.isFoundInValidMoves();
      if(board.getPiece(move).getTeamColor() != teamTurn) {
        throw new InvalidMoveException("Invalid move cannot be made");
      }
      board.makeMove(move);

      teamTurn = (teamTurn == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return ChessRuleBook.isInCheck(board, teamColor);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return ChessRuleBook.isInCheckMate(board, teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return ChessRuleBook.isInStaleMate(board, teamColor);
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
