package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

  //TODO Reorganize structure such that 'validMoves' doesn't need to exist
  private ChessBoard board;
  private TeamColor teamTurn;
  private Collection<ChessMove> validMoves;

  /**
   * Constructs a ChessGame object.
   */
  public ChessGame() {
    this.validMoves = new HashSet<>();
    this.board = new ChessBoard();
    this.teamTurn = TeamColor.WHITE;
  }

  /**
   * Constructs a ChessGame object based on a board and player turn.
   *
   * @param board     chess board
   * @param teamTurn  team turn
   */
  public ChessGame(ChessBoard board, TeamColor teamTurn) {
    try {
      this.board = board.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
    this.teamTurn = teamTurn;
    populateAllValidMoves();
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
   * Populates the validMoves collection with all the validMoves for the given board
   */
  public void populateAllValidMoves() {
    resetValidMoves();
    Collection<ChessPosition> friendlyPositions = board.iterateForFriendlyPieces(teamTurn);
    for (ChessPosition position : friendlyPositions) {
      validMoves.addAll(validMoves(position));
    }
  }


  /**
   * Resets the valid moves set
   */
  public void resetValidMoves() {
    validMoves = new HashSet<>();
  }


  /**
   * Makes a move in a chess game if it is valid.
   *
   * @param move chess move to update 'board' attribute with
   * @throws InvalidMoveException if move is invalid
   */
  public void makeMove(ChessMove move) throws InvalidMoveException {
    populateAllValidMoves();
    if (validMoves.contains(move)) {
      board.makeMove(move);
      switchTurn();
    } else {
      throw new InvalidMoveException();
    }
  }


  /**
   * Switches the teamTurn to the next team
   */
  private void switchTurn() {
    teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
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


  /**
   * @return  Current team color
   */
  public TeamColor getTeamTurn() {
    return teamTurn;
  }
}
