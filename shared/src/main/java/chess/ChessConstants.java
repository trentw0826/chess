package chess;

import java.util.EnumSet;

/**
 * Class of chess constants
 */
public class ChessConstants {

  protected static final int[][] ALL_DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
  protected static final int[][] ROOK_DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
  protected static final int[][] BISHOP_DIRECTIONS = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
  protected static final int[][] KNIGHT_DIRECTIONS = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};

  protected static final EnumSet<ChessPiece.PieceType> PROMOTION_TYPES = EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
          ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT);

  protected static final int BOARD_SIZE = 8;

  // Private constructor to avoid compiler's implicit one
  private ChessConstants() { throw new AssertionError("ChessConstants class should not be instantiated"); }
}

