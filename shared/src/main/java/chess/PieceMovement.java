package chess;

import java.util.ArrayList;

/**
 * Parent class used to generate all possible moves of a given piece
 */
public abstract class PieceMovement {
  protected ChessPiece.PieceType type;
  protected ChessBoard board;
  protected ChessPosition position;
  protected ArrayList<ChessMove> possibleMoves;

  protected abstract void generateMoves();
  public abstract ArrayList<ChessMove> pieceMoves();
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
  public King(ChessBoard board, ChessPosition position) {
    type = ChessPiece.PieceType.KING;
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
  public ArrayList<ChessMove> pieceMoves() {
    return new ArrayList<>();
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
  public Queen(ChessBoard board, ChessPosition position) {
    type = ChessPiece.PieceType.QUEEN;
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
  public ArrayList<ChessMove> pieceMoves() {
    return new ArrayList<>();
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
  public Rook(ChessBoard board, ChessPosition position) {
    type = ChessPiece.PieceType.ROOK;
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
  public ArrayList<ChessMove> pieceMoves() {
    return new ArrayList<>();
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
  public Bishop(ChessBoard board, ChessPosition position) {
    type = ChessPiece.PieceType.BISHOP;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for BISHOP (based on 'board' and 'position' attributes)
   */
  @Override
  protected void generateMoves() {

  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public ArrayList<ChessMove> pieceMoves() {
    return new ArrayList<>();
  }
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
  public Knight(ChessBoard board, ChessPosition position) {
    type = ChessPiece.PieceType.KNIGHT;
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
  public ArrayList<ChessMove> pieceMoves() {
    return new ArrayList<>();
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
  public Pawn(ChessBoard board, ChessPosition position) {
    type = ChessPiece.PieceType.PAWN;
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
  public ArrayList<ChessMove> pieceMoves() {
    return new ArrayList<>();
  }
}