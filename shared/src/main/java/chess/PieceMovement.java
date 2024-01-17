package chess;

import java.util.HashSet;

/**
 * Parent class used to generate all possible moves of a given piece
 */
public abstract class PieceMovement {
  protected ChessPiece.PieceType type;
  protected ChessGame.TeamColor color;
  protected ChessBoard board;
  protected ChessPosition position;
  protected HashSet<ChessMove> possibleMoves = new HashSet<>();

  protected abstract void generateMoves();
  public abstract HashSet<ChessMove> getPossibleMoves();

  /**
   * @return  If the move is on the board and doesn't end on a friendly piece
   */
  protected boolean validateMove(ChessMove move) {
    ChessPosition endPosition=move.getEndPosition();
    ChessPiece endPiece=board.getPiece(endPosition);

    // The end position on the board and is either empty or occupied by an enemy piece
    if (move.moveIsOnBoard()) {
      return endPiece == null || endPiece.getTeamColor() != color;
    }
    return false;
  }
}

/**
 * Generate and store moves for the KING found at given position on the given board
 */
class King extends PieceMovement {
  /**
   * Constructor generates KING piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public King(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ChessPiece.PieceType.KING;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for KING (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {

  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }
}

/**
 * Generate and store moves for the QUEEN found at given position on the given board
 */
class Queen extends PieceMovement {
  /**
   * Constructor generates QUEEN piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public Queen(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ChessPiece.PieceType.QUEEN;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for QUEEN (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {

  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }
}

/**
 * Generate and store moves for the ROOK found at given position on the given board
 */
class Rook extends PieceMovement {
  /**
   * Constructor generates ROOK piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public Rook(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ChessPiece.PieceType.ROOK;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for ROOK (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {

  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }
}

/**
 * Generate and store moves for the BISHOP found at given position on the given board
 */
class Bishop extends PieceMovement {
  /**
   * Constructor generates BISHOP piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public Bishop(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ChessPiece.PieceType.BISHOP;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for BISHOP (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {
    // upper right direction
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(i, i), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(i, i);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
    // lower right direction
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(-i, i), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(-i, i);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
    // lower right direction
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(i, -i), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(i, -i);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
    // lower left direction
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(-i, -i), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(-i, -i);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() { return possibleMoves; }
}

/**
 * Generate and store moves for the KNIGHT found at given position on the given board
 */
class Knight extends PieceMovement {
  /**
   * Constructor generates KNIGHT piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public Knight(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ChessPiece.PieceType.KNIGHT;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for KNIGHT (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {

  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }
}

/**
 * Generate and store moves for the PAWN found at given position on the given board
 */
class Pawn extends PieceMovement {
  /**
   * Constructor generates PAWN piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public Pawn(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ChessPiece.PieceType.PAWN;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for PAWN (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {

  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }
}
