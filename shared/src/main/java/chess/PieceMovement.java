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
    ChessPosition endPosition = move.getEndPosition();
    ChessPiece endPiece = board.getPiece(endPosition);

    // The end position on the board and is either empty or occupied by an enemy piece (not a pawn)
    if (move.moveIsOnBoard()) {
      return endPiece == null || endPiece.getTeamColor() != color;
    } else return false;
  }

  protected boolean validatePawnMove(ChessMove move) {
    ChessPosition startPosition = move.getStartPosition();
    ChessPosition endPosition = move.getEndPosition();
    ChessPiece endPiece = board.getPiece(endPosition);

    // If the move is empty
    if (endPiece == null) {
      return (move.moveIsOnBoard() && startPosition.getColumn() == endPosition.getColumn());
    }
    // If capturing diagonally
    else {
      int rowDifference = Math.abs(endPosition.getRow() - startPosition.getRow());
      int colDifference = Math.abs(endPosition.getColumn() - startPosition.getColumn());
      return rowDifference == 1 && colDifference == 1 && endPiece.getTeamColor() != color;
    }
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
    //TODO: Update function with avoidance of pieces with enemy vision

    // Double nested loop checks every square next to the king
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) continue;
        ChessPosition endPosition = position.relativePosition(i-1,j-1);
        ChessMove move = new ChessMove(position, endPosition, null);
        if (validateMove(move)) {
          possibleMoves.add(move);
        }
      }
    }
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
  // TODO: create method
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
    // going up
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(i, 0), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(i, 0);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
    // going down
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(-i, 0), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(-i, 0);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
    // going left
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(0, -i), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(0, -i);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
    // going right
    for (int i = 1; validateMove(new ChessMove(position, position.relativePosition(0, i), null)); i++) {
      ChessPosition newEndPosition = position.relativePosition(0, i);
      possibleMoves.add(new ChessMove(position, newEndPosition, null));
      if (board.getPiece(newEndPosition) != null) { break; }
    }
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
  final int[][] knightMoveOffsets = {
          {-2, -1}, {-1, -2}, {1, -2}, {2, -1},
          {2, 1}, {1, 2}, {-1, 2}, {-2, 1}
  };

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
    for (int[] arr : knightMoveOffsets) {
      ChessPosition endPosition = position.relativePosition(arr[0], arr[1]);
      ChessMove move = new ChessMove(position, endPosition, null);
      if (validateMove(move)) possibleMoves.add(move);
    }
  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() { return possibleMoves; }
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
      int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;

      // Single square move
      ChessMove oneSquareMove = new ChessMove(position, position.relativePosition(direction, 0), null);
      if (validatePawnMove(oneSquareMove)) {
        possibleMoves.add(oneSquareMove);

        // Double square move for starting position
        int startingRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int doubleMoveOffset = 2 * direction;
        ChessMove doubleMove = new ChessMove(position, position.relativePosition(doubleMoveOffset, 0), null);
        if (position.getRow() == startingRow && validatePawnMove(doubleMove)) {
          possibleMoves.add(doubleMove);
        }
      }

      // Side captures
      int[] sideOffsets = {(color == ChessGame.TeamColor.WHITE) ? 1 : -1, 1};
      for (int offset : sideOffsets) {
        ChessMove sideMove = new ChessMove(position, position.relativePosition(direction, offset), null);
        if (validatePawnMove(sideMove)) {
          possibleMoves.add(sideMove);
        }
      }
      // TODO: Add promotion logic
  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }
}
