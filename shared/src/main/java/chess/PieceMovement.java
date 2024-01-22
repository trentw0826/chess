package chess;

import java.util.HashSet;

/**
 * Parent class used to generate all possible moves of a given piece
 */
public abstract class PieceMovement {
  final ChessPiece.PieceType[] promotionTypes = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
          ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
  final int[][] knightMoveOffsets = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};

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

  /**
   * Takes a move and adds it to possibleMoves if valid
   * @param move move to be tested
   * @return if move was added
   */
  protected boolean addMoveIfValid(ChessMove move) {
    // If the move is with a pawn AND it's a valid pawn move
    if (board.getPiece(move).getPieceType() == ChessPiece.PieceType.PAWN) {
      if (validatePawnMove(move)) {
        possibleMoves.add(move);
        return true;
      } else return false;
    } else if (validateMove(move)) {
      possibleMoves.add(move);
      return true;
    } else return false;
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
    // Double nested loop checks every square next to the king
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) continue;
        ChessPosition endPosition = position.getRelativePosition(i-1,j-1);
        ChessMove move = new ChessMove(position, endPosition, null);
        addMoveIfValid(move);
      }
    }
  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() { return possibleMoves; }
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
    int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      boolean continueDirection = true;
      int i = 1;

      while (continueDirection) {
        ChessPosition newEndPosition = position.getRelativePosition(i * rowOffset, i * colOffset);
        if (!addMoveIfValid(new ChessMove(position, newEndPosition, null)) ||
                (board.getPiece(newEndPosition) != null && board.getPiece(newEndPosition).getTeamColor() != color)) {
          continueDirection = false;
        }

        i++;
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
    int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};

    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      boolean continueDirection = true;
      int i = 1;

      while (continueDirection) {
        ChessPosition newEndPosition = position.getRelativePosition(i * rowOffset, i * colOffset);
        if (!addMoveIfValid(new ChessMove(position, newEndPosition, null)) ||
                (board.getPiece(newEndPosition) != null && board.getPiece(newEndPosition).getTeamColor() != color)) {
          continueDirection = false;
        }

        i++;
      }
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
    for (int[] arr : knightMoveOffsets) {
      ChessPosition endPosition = position.getRelativePosition(arr[0], arr[1]);
      ChessMove move = new ChessMove(position, endPosition, null);
      addMoveIfValid(move);
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
    int currRow = position.getRow();
    int startingRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
    int endRow = (color == ChessGame.TeamColor.WHITE) ? 7 : 2;

    ChessMove oneSquareMove = new ChessMove(position, direction, 0, null);

    if (currRow == startingRow) {
      // Pawn hasn't moved yet
      if (addMoveIfValid(oneSquareMove)) {
        ChessMove doubleMove = new ChessMove(position, 2 * direction, 0, null);
        addMoveIfValid(doubleMove);

        for (int offset : new int[]{-1, 1}) {
          ChessMove sideMove = new ChessMove(position, direction, offset, null);
          addMoveIfValid(sideMove);
        }
      }
    } else if (currRow == endRow) {
      // Pawn is about to promote
      if (validatePawnMove(oneSquareMove)) {
        for (ChessPiece.PieceType promotionPiece : promotionTypes) {
          possibleMoves.add(new ChessMove(position, direction, 0, promotionPiece));
        }
      }
      for (int offset : new int[]{-1, 1}) {
        ChessMove sideMove = new ChessMove(position, direction, offset, null);
        if (validatePawnMove(sideMove)) {
          for (ChessPiece.PieceType promotionPiece : promotionTypes) {
            possibleMoves.add(new ChessMove(position, direction, offset, promotionPiece));
          }
        }
      }

    } else {
      // Pawn is in between start and promotion
      addMoveIfValid(oneSquareMove);
      for (int offset : new int[]{-1, 1}) {
        ChessMove sideMove = new ChessMove(position, direction, offset, null);
        addMoveIfValid(sideMove);
      }
    }
  }

  /**
   * @return the possibleMoves array
   */
  @Override
  public HashSet<ChessMove> getPossibleMoves() { return possibleMoves; }
}
