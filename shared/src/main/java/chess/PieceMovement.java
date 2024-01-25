package chess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.EnumSet;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;


/**
 * Parent class used to generate all possible moves of a given piece.
 */
public abstract class PieceMovement {

  protected static final EnumSet<ChessPiece.PieceType> PROMOTION_TYPES = EnumSet.of(QUEEN, ROOK, BISHOP, KNIGHT);

  protected static final int[][] ALL_DIRECTIONS= {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
  protected static final int[][] BISHOP_DIRECTIONS = Arrays.copyOfRange(ALL_DIRECTIONS, 4, 8);
  protected static final int[][] ROOK_DIRECTIONS = Arrays.copyOfRange(ALL_DIRECTIONS, 0, 4);
  protected static final int[][] KNIGHT_DIRECTIONS = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};

  protected ChessPiece.PieceType type;
  protected ChessGame.TeamColor color;
  protected ChessBoard board;
  protected ChessPosition position;
  protected HashSet<ChessMove> possibleMoves = new HashSet<>();

  /**
   * Constructs a PieceMovement object
   * @param type      piece type
   * @param color     piece color
   * @param board     chess board
   * @param position  position on the given chess board
   */
  protected PieceMovement(ChessPiece.PieceType type, ChessGame.TeamColor color, ChessBoard board, ChessPosition position) {

    this.type = type;
    this.color = color;
    this.board = board;
    this.position = position;
  }


  /**
   * @return 'possibleMoves' set
   */
  public Set<ChessMove> getPossibleMoves() {
    return possibleMoves;
  }


  /**
   * Generate all possible sliding moves for the given directions (queens, bishops, rooks).
   * For each direction (int[] size 2), loop over extensions of that direction (dir * 1, dir * 2, etc.) in
   * the form of relative moves. If the end position is: empty -> add & continue loop; friendly piece -> break extension loop;
   * enemy piece -> add & break extension loop.
   *
   * @param directions  double array of directions to be checked
   */
  protected void generateDirectionalMoves(int[][] directions) {

    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      int i = 1;

      while (true) {
        ChessMove move = new ChessMove(position, i * rowOffset, i * colOffset);

        if (!(move.moveIsWithinBounds() && board.landsOnEmpty(move))) {
          if (board.landsOnEnemy(move)) {
            // Ends with enemy
            addMove(move);
          }
          // Ends with enemy or friend
          break;
        }
        // Ends empty
        addMove(move);
        i++;
      }
    }
  }


  /**
   * Generate all possible discrete moves for the given directions (kings and knights).
   *
   * @param directions  provided array of directions
   */
  protected void generateDiscreteMoves(int[][] directions) {

    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      addMoveIfValid(new ChessMove(position, rowOffset, colOffset));
    }
  }


  /**
   * @return If the move is on the board and doesn't end on a friendly piece
   */
  protected boolean validateMove(ChessMove move) {

    if (board.getPiece(move).getPieceType() == PAWN) {
      return validatePawnMove(move);
    } else {
      // The end position on the board and is either empty or occupied by an enemy piece
      return move.moveIsWithinBounds() && (board.landsOnEnemy(move) || board.landsOnEmpty(move));
    }
  }


  /**
   * Validates a pawn move.
   * A pawn move is valid if: it moves once to an empty square, it moves twice to an empty square
   * (passing through an empty square), it moves up and to the side to an enemy-occupied square
   *
   * @param move  ChessMove object to be checked
   * @return      If pawn move is valid
   */
  protected boolean validatePawnMove(ChessMove move) {

    int direction = (board.getPiece(move).getTeamColor() == WHITE) ? 1 : -1;

    ChessPosition startPosition = move.getStartPosition();
    ChessPosition endPosition = move.getEndPosition();

    int rowDifference = Math.abs(endPosition.getRow() - startPosition.getRow());
    int colDifference = Math.abs(endPosition.getColumn() - startPosition.getColumn());

    if (rowDifference == 1) {
      if (colDifference == 1) {       // Sideways capture
        return board.landsOnEnemy(move);
      }
      else if (colDifference == 0) {  // Single forward push
        return board.landsOnEmpty(move);
      }
      else {                          // Invalid colDifference
        return false;
      }
    }
    else if (rowDifference == 2) {    // Double forward push
      ChessMove doubleMove = new ChessMove(move.getStartPosition(), 2 * direction, 0);
      return board.landsOnEmpty(move) && board.landsOnEmpty(doubleMove);
    }
    return false;                     // Invalid pawn move
  }


  /**
   * Takes a move and adds it to possibleMoves if valid
   * @param move move to be tested
   * @return if move was added
   */
  protected boolean addMoveIfValid(ChessMove move) {

    if (validateMove(move)) {
      addMove(move);
      return true;
    }
    return false;
  }

  /**
   * Add move to 'possibleMoves' set.
   *
   * @param move  move to be added
   */
  protected void addMove(ChessMove move) {
    possibleMoves.add(move);
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

    super(KING, color, board, position);
    generateMoves();
  }


  /**
   * Populate the possibleMoves array with all possible moves for KING (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {
    generateDiscreteMoves(ALL_DIRECTIONS);
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

    super(QUEEN, color, board, position);
    generateMoves();
  }


  /**
   * Populate the possibleMoves array with all possible moves for QUEEN (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {
    generateDirectionalMoves(ALL_DIRECTIONS);
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

    super(ROOK, color, board, position);
    generateMoves();
  }


  /**
   * Populate the possibleMoves array with all possible moves for ROOK (based on 'board' and 'position' attributes).
   */
  private void generateMoves() {
    generateDirectionalMoves(ROOK_DIRECTIONS);
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

    super(BISHOP, color, board, position);
    generateMoves();
  }


  /**
   * Populate the possibleMoves array with all possible moves for BISHOP (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {
    generateDirectionalMoves(BISHOP_DIRECTIONS);
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

    super(KNIGHT, color, board, position);
    generateMoves();
  }


  /**
   * Populate the possibleMoves array with all possible moves for KNIGHT (based on 'board' and 'position' attributes)
   */
  private void generateMoves() {
    generateDiscreteMoves(KNIGHT_DIRECTIONS);
  }
}


/**
 * Generate and store moves for the PAWN found at given position on the given board.
 */
class Pawn extends PieceMovement {

  int direction;
  boolean onStartRow;
  boolean onEndRow;

  /**
   * Constructor generates PAWN piece and its possible moves based on 'board' and 'position'.
   *
   * @param board     Given board
   * @param position  Given position
   */
  public Pawn(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {

    super(PAWN, color, board, position);

    boolean pawnIsWhite = (color == WHITE);

    this.direction = pawnIsWhite ? 1 : -1;
    onStartRow = (pawnIsWhite ? 2 : 7) == position.getRow();
    onEndRow = (pawnIsWhite ? 7 : 2) == position.getRow();

    generateMoves();
  }


  /**
   * Generate all pawn moves
   */
  private void generateMoves() {

    if (onEndRow) {
      generatePromotionMoves();
    } else {
      generateNonPromotionMoves();
    }
  }

  /**
   * Generate pawn moves. Only will be called if pawn is not on end row
   */
  private void generateNonPromotionMoves() {

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);

    if (addMoveIfValid(oneSquareMove) && onStartRow) {
      ChessMove doubleMove = new ChessMove(position, 2 * direction, 0);
      addMoveIfValid(doubleMove);
    }

    generateSideMoves();
  }


  /**
   * Generate possible side capture moves
   */
  private void generateSideMoves() {

    for (int offset : new int[]{-1, 1}) {
      ChessMove sideMove = new ChessMove(position, direction, offset);
      addMoveIfValid(sideMove);
    }
  }


  /**
   * Generate possible promotion moves if the pawn is found on the final row
   */
  private void generatePromotionMoves() {

    for (int offset : new int[]{-1, 0, 1}) {
      ChessMove move = new ChessMove(position, direction, offset);
      if (validateMove(move)) {
        for (ChessPiece.PieceType piece : PROMOTION_TYPES) {
          possibleMoves.add(new ChessMove(position, direction, offset, piece));
        }
      }
    }
  }
}
