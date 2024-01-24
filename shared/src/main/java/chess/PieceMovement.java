package chess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * Parent class used to generate all possible moves of a given piece.
 */
public abstract class PieceMovement {
  protected static final ChessPiece.PieceType[] PROMOTION_TYPES= {QUEEN, ROOK, BISHOP, KNIGHT};

  protected static final int[][] KNIGHT_DIRECTIONS= {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
  protected static final int[][] SLIDING_DIRECTIONS= {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
  protected static final int[][] BISHOP_DIRECTIONS= Arrays.copyOfRange(SLIDING_DIRECTIONS, 4, 8);
  protected static final int[][] ROOK_DIRECTIONS= Arrays.copyOfRange(SLIDING_DIRECTIONS, 0, 4);

  protected ChessPiece.PieceType type;
  protected ChessGame.TeamColor color;
  protected ChessBoard board;
  protected ChessPosition position;
  protected HashSet<ChessMove> possibleMoves = new HashSet<>();

  public abstract Set<ChessMove> getPossibleMoves();

  /**
   * Populate 'possibleMoves' based on an int[][] of possible directions.
   *
   * @param directions  double array of possible directions
   */
  protected void generateDirectionalMoves(int[][] directions) {
    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      int i = 1;

      while (true) {
        ChessPosition newEndPosition = position.getRelativePosition(i * rowOffset, i * colOffset);
        ChessMove move = new ChessMove(position, newEndPosition);
        if (!addMoveIfValid(move) || (!board.endPositionIsEmpty(move) && board.endPositionIsEnemy(move))) {
          break;
        }
        i++;
      }
    }
  }

  /**
   * @return If the move is on the board and doesn't end on a friendly piece
   */
  protected boolean validateMove(ChessMove move) {
    // The end position on the board and is either empty or occupied by an enemy piece (not a pawn)
     return move.moveIsOnBoard() && (board.endPositionIsEnemy(move) || board.endPositionIsEmpty(move));
  }

  /**
   * Validates a pawn move.
   *
   * @param move  ChessMove object to be checked
   * @return      true if: The move is a single push towards a blank square, it's a diagonal push towards an enemy-occupied square, or it's a double move that moves through two blank squares.
   */
  protected boolean validatePawnMove(ChessMove move) {
    if (board.getPiece(move).getPieceType()!= PAWN) {
      throw new IllegalArgumentException("validatePawnMove only accepts pawn moves");
    }

    ChessPosition startPosition = move.getStartPosition();
    ChessPosition endPosition = move.getEndPosition();

    int rowDifference = Math.abs(endPosition.getRow() - startPosition.getRow());
    int colDifference = Math.abs(endPosition.getColumn() - startPosition.getColumn());

    // Pawn move is within theoretical bounds
    if (rowDifference < 1 || rowDifference >= 3 || colDifference > 1) {
      return false;
    }

    int direction = (board.getPiece(move).getTeamColor() == WHITE) ? 1 : -1;

    if (rowDifference == 1) {
      if (colDifference == 1) {
        // Sideways capture
        return board.endPositionIsEnemy(move);
      } else {
        // Single push forward
        return board.endPositionIsEmpty(move);
      }
    } else {
      // Double push forward
      ChessMove doubleMove = new ChessMove(move.getStartPosition(), 2 * direction, 0);
      return board.endPositionIsEmpty(move) && board.endPositionIsEmpty(doubleMove);
    }
  }


  /**
   * Takes a move and adds it to possibleMoves if valid
   * @param move move to be tested
   * @return if move was added
   */
  protected boolean addMoveIfValid(ChessMove move) {
    if (board.getPiece(move).getPieceType() == PAWN) {
      return validatePawnMove(move) && possibleMoves.add(move);
    } else {
      return validateMove(move) && possibleMoves.add(move);
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

    for (int[] direction : SLIDING_DIRECTIONS) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      addMoveIfValid(new ChessMove(position, rowOffset, colOffset));
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
    generateDirectionalMoves(SLIDING_DIRECTIONS);
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
    generateDirectionalMoves(ROOK_DIRECTIONS);
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
    generateDirectionalMoves(BISHOP_DIRECTIONS);
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
    for (int[] direction : KNIGHT_DIRECTIONS) {
      ChessPosition endPosition = position.getRelativePosition(direction[0], direction[1]);
      ChessMove move = new ChessMove(position, endPosition);
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
  int direction;
  int startRow;
  int endRow;

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

    if (color == WHITE) {
      direction = 1;
      startRow = 2;
      endRow = 7;
    } else {
      direction = -1;
      startRow = 7;
      endRow = 2;
    }
    generateMoves();
  }

  private void generateMoves() {

    int row = position.getRow();

    if (row == endRow) {
      // Pawn is about to promote
      generatePromotionMoves();
    } else {
      if (row == startRow) {
        // Pawn is on starting square
        generateInitialMoves();
      } else {
        // Pawn is somewhere in the middle
        generateNonInitialMoves();
      }
    }
  }

  private void generateInitialMoves() {

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);
    ChessMove doubleMove = new ChessMove(position, 2 * direction, 0);

    if (addMoveIfValid(oneSquareMove)) {
      addMoveIfValid(doubleMove);
    }

    generateSideMoves();
  }

  private void generateSideMoves() {
    for (int offset : new int[]{-1, 1}) {
      ChessMove sideMove = new ChessMove(position, direction, offset);
      addMoveIfValid(sideMove);
    }
  }

  private void generateNonInitialMoves() {

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);

    addMoveIfValid(oneSquareMove);

    generateSideMoves();
  }

  private void generatePromotionMoves() {
    if (position.getRow() != endRow) {
      throw new IllegalArgumentException("generatePromotionMoves should only be called for a pawn about to promote");
    }

    for (int offset : new int[] {-1, 0, 1}) {
      if (validatePawnMove(new ChessMove(position, direction, offset))) {
        for (ChessPiece.PieceType piece : PROMOTION_TYPES) {
          possibleMoves.add(new ChessMove(position, direction, offset, piece));
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
