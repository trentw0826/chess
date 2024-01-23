package chess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static chess.ChessPiece.PieceType.*;

/**
 * Parent class used to generate all possible moves of a given piece.
 */
public abstract class PieceMovement {
  final ChessPiece.PieceType[] promotionTypes = {QUEEN, ROOK, BISHOP, KNIGHT};

  final int[][] knightDirections= {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
  final int[][] slidingDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
  final int[][] bishopDirections = Arrays.copyOfRange(slidingDirections, 4, 8);
  final int[][] rookDirections = Arrays.copyOfRange(slidingDirections, 0, 4);

  protected ChessPiece.PieceType type;
  protected ChessGame.TeamColor color;
  protected ChessBoard board;
  protected ChessPosition position;
  protected HashSet<ChessMove> possibleMoves = new HashSet<>();

  public abstract Set<ChessMove> getPossibleMoves();

  /**
   * @return If the move is on the board and doesn't end on a friendly piece
   */
  protected boolean validateMove(ChessMove move) {
    ChessPosition endPosition = move.getEndPosition();
    ChessPiece endPiece = board.getPiece(endPosition);

    // The end position on the board and is either empty or occupied by an enemy piece (not a pawn)
    if (move.moveIsOnBoard()) {
      return endPiece == null || endPiece.getTeamColor() != color;
    }
    return false;
  }

  /**
   * Validates a pawn move.
   *
   * @param move ChessMove object to be checked
   * @return valid if: It's a single push towards a blank square, it's a diagonal push towards an
   *    * enemy-occupied square, or it's a double move that moves through two blank squares.
   */
  protected boolean validatePawnMove(ChessMove move) {
    if (board.getPiece(move).getPieceType() != PAWN) {
      throw new IllegalArgumentException("validatePawnMove only accepts pawn moves");
    }

    ChessPosition startPosition = move.getStartPosition();
    ChessPosition endPosition = move.getEndPosition();

    ChessPiece endPiece = board.getPiece(endPosition);

    boolean endSquareEmpty = board.endPositionEmpty(move);

    int rowDifference = Math.abs(endPosition.getRow() - startPosition.getRow());
    int colDifference = Math.abs(endPosition.getColumn() - startPosition.getColumn());

    int startRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;

    // Move must exist within the bounds of a double move/diagonal move to be valid
    if (rowDifference < 1 || rowDifference >= 3 || colDifference >= 2) {
      return false;
    }

    if (rowDifference == 2) {
      // Double move
      ChessPosition oneSquareAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
      return startPosition.getRow() == startRow
              && (board.positionIsEmpty(oneSquareAhead) && board.positionIsEmpty(endPosition));
    }
    else if (colDifference == 1) {
      return (!endSquareEmpty && endPiece.getTeamColor() != color);
    }
    else {
      return (endSquareEmpty);
    }
  }

  /**
   * Takes a move and adds it to possibleMoves if valid
   * @param move move to be tested
   * @return if move was added
   */
  protected boolean addMoveIfValid(ChessMove move) {
    // If the move is with a pawn AND it's a valid pawn move
    if (board.getPiece(move).getPieceType() == PAWN) {
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
    type = KING;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for KING (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {

    for (int[] direction : slidingDirections) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      ChessPosition newEndPosition = position.getRelativePosition(rowOffset, colOffset);
      addMoveIfValid(new ChessMove(position, newEndPosition, null));
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
 * Generate and store moves for the QUEEN found at given position on the given board.
 */
class Queen extends PieceMovement {
  /**
   * Constructor generates QUEEN piece and its possible moves based on 'board' and 'position'.
   *
   * @param board     Given board
   * @param position  Given position
   */
  public Queen(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = QUEEN;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for QUEEN (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {

    for (int[] direction : slidingDirections) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      boolean continueDirection = true;
      int i = 1;

      while (continueDirection) {
        ChessPosition newEndPosition = position.getRelativePosition(i * rowOffset, i * colOffset);
        if (!addMoveIfValid(new ChessMove(position, newEndPosition, null))
                || (board.getPiece(newEndPosition) != null && board.getPiece(newEndPosition).getTeamColor() != color)) {
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
 * Generate and store moves for the ROOK found at given position on the given board.
 */
class Rook extends PieceMovement {
  /**
   * Constructor generates ROOK piece and its possible moves based on 'board' and 'position'.
   *
   * @param board     Given board
   * @param position  Given position
   */
  public Rook(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = ROOK;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for ROOK (based on 'board' and 'position' attributes).
   */
  private void generateMoves() {

    for (int[] direction : rookDirections) {
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
    type = BISHOP;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for BISHOP (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {

    for (int[] direction : bishopDirections) {
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
 * Generate and store moves for the KNIGHT found at given position on the given board
 */
class Knight extends PieceMovement {
  /**
   * Constructor generates KNIGHT piece and its possible moves based on 'board' and 'position'
   * @param board     Given board
   * @param position  Given position
   */
  public Knight(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = KNIGHT;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for KNIGHT (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {
    for (int[] arr : knightDirections) {
      ChessPosition endPosition = position.getRelativePosition(arr[0], arr[1]);
      ChessMove move = new ChessMove(position, endPosition, null);
      addMoveIfValid(move);
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
 * Generate and store moves for the PAWN found at given position on the given board.
 */
class Pawn extends PieceMovement {
  /**
   * Constructor generates PAWN piece and its possible moves based on 'board' and 'position'.
   *
   * @param board     Given board
   * @param position  Given position
   */
  public Pawn(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    type = PAWN;
    this.color = color;
    this.board = board;
    this.position = position;
    generateMoves();
  }

  /**
   * Populate the possibleMoves array with all possible moves for PAWN (based on 'board' and 'position' attributes).
   */
  private void generateMoves() {
    int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
    int currRow = position.getRow();
    int startingRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
    int endRow = (color == ChessGame.TeamColor.WHITE) ? 7 : 2;

    ChessMove oneSquareMove = new ChessMove(position, direction, 0, null);
    ChessMove doubleMove = new ChessMove(position, 2 * direction, 0, null);

    if (currRow == startingRow) {
      // Pawn hasn't moved yet
      if (addMoveIfValid(oneSquareMove)) {
        addMoveIfValid(doubleMove);
      }

      for (int offset : new int[]{-1, 1}) {
        ChessMove sideMove = new ChessMove(position, direction, offset, null);
        addMoveIfValid(sideMove);
      }
    } else if (currRow == endRow) {
      // Pawn is about to promote
      promotePawnMoves(oneSquareMove);
      for (int offset : new int[]{-1, 1}) {
        ChessMove sideMove = new ChessMove(position, direction, offset, null);
        promotePawnMoves(sideMove);
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
   * Update possibleMoves with promotion moves when necessary.
   *
   * @param move a pawn promotion move
   */
  private void promotePawnMoves(ChessMove move) {
    if (validatePawnMove(move)) {
      for (ChessPiece.PieceType piece : promotionTypes) {
        possibleMoves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), piece));
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
