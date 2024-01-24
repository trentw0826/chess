package chess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static chess.ChessPiece.PieceType.*;

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
  public void generateDirectionalMoves(int[][] directions) {
    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      boolean continueDirection = true;
      int i = 1;

      while (continueDirection) {
        ChessPosition newEndPosition = position.getRelativePosition(i * rowOffset, i * colOffset);
        if (!addMoveIfValid(new ChessMove(position, newEndPosition))
                || (!board.positionIsEmpty(newEndPosition) && isEnemyPosition(newEndPosition))) {
          continueDirection = false;
        }
        i++;
      }
    }
  }

  /**
   * @param position  ChessPosition to check for enemy occupation
   * @return          if the position holds an enemy piece
   */
  private boolean isEnemyPosition(ChessPosition position) {
    ChessPiece endPiece = board.getPiece(position);
    return endPiece != null && endPiece.getTeamColor() != color;
  }

  /**
   * @param position  ChessPosition to check for emptiness
   * @return          if the position is empty
   */
  private boolean isEmptyPosition(ChessPosition position) {
    return board.getPiece(position) == null;
  }

  /**
   * @return If the move is on the board and doesn't end on a friendly piece
   */
  protected boolean validateMove(ChessMove move) {
    ChessPosition endPosition = move.getEndPosition();

    // The end position on the board and is either empty or occupied by an enemy piece (not a pawn)
     return move.moveIsOnBoard() && (isEnemyPosition(endPosition) || isEmptyPosition(endPosition));
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

    ChessPiece endPiece = board.getPiece(endPosition);

    boolean endSquareEmpty = board.endPositionEmpty(move);

    int rowDifference = Math.abs(endPosition.getRow() - startPosition.getRow());
    int colDifference = Math.abs(endPosition.getColumn() - startPosition.getColumn());

    int startRow = (color == ChessGame.TeamColor.WHITE)? 2 : 7;

    if (rowDifference < 1 || rowDifference >= 3 || colDifference >= 2) {
      return false;
    }

    if (rowDifference == 2) {
      ChessPosition oneSquareAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
      return startPosition.getRow() == startRow
              && (board.positionIsEmpty(oneSquareAhead) && board.positionIsEmpty(endPosition));
    } else if (colDifference == 1) {
      return (!endSquareEmpty && endPiece.getTeamColor()!= color);
    } else {
      return endSquareEmpty;
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

      ChessPosition newEndPosition = position.getRelativePosition(rowOffset, colOffset);
      addMoveIfValid(new ChessMove(position, newEndPosition));
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

  public void generateMoves() {
    int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
    int startRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
    int endRow = (color == ChessGame.TeamColor.WHITE) ? 7 : 2;

    int row = position.getRow();

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);

    if (row == endRow) {
      // Pawn is about to promote
      if (validatePawnMove(oneSquareMove)) {
        generatePromotionMoves(oneSquareMove);
      }
      for (int offset : new int[]{-1, 1}) {
        ChessMove sideMove = new ChessMove(position, direction, offset);
        generatePromotionMoves(sideMove);
      }
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
    int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
//    int startingRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;

//    int row = position.getRow();

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);
    ChessMove doubleMove = new ChessMove(position, 2 * direction, 0);

    if (addMoveIfValid(oneSquareMove)) {
      addMoveIfValid(doubleMove);
    }

    for (int offset : new int[]{-1, 1}) {
      ChessMove sideMove = new ChessMove(position, direction, offset);
      addMoveIfValid(sideMove);
    }
  }

  private void generateNonInitialMoves() {
    int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);

    addMoveIfValid(oneSquareMove);

    for (int offset : new int[]{-1, 1}) {
      ChessMove sideMove = new ChessMove(position, direction, offset);
      addMoveIfValid(sideMove);
    }
  }

  private void generatePromotionMoves(ChessMove move) {
    if (validatePawnMove(move)) {
      for (ChessPiece.PieceType piece : PROMOTION_TYPES) {
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
