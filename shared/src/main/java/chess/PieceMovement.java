package chess;

import java.util.*;

import static chess.ChessGame.TeamColor.*;


/**
 * Given a board and position, calculates and holds all the possible moves of the piece at that position
 */
public interface PieceMovement {

  /**
   * @return set of all possible moves for the given board at the given position.
   */
  static Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition position) {
    Collection<ChessMove> moves = new HashSet<>();

    ChessPiece piece = board.getPiece(position);
    if (piece == null) return moves;

    switch (piece.getPieceType()) {
      case KING -> moves = King.generateMoves(board, position);
      case QUEEN -> moves = Queen.generateMoves(board, position);
      case BISHOP -> moves = Bishop.generateMoves(board, position);
      case ROOK -> moves = Rook.generateMoves(board, position);
      case KNIGHT -> moves = Knight.generateMoves(board, position);
      case PAWN -> moves = Pawn.generateMoves(board, position);
      default -> throw new IllegalArgumentException("Illegal PieceType accessed");
    }

    return moves;
  }


  /**
   * Generate all possible sliding moves for the given directions (queens, bishops, rooks).
   * For each direction (int[] size 2), loop over extensions of that direction (dir * 1, dir * 2, etc.) in
   * the form of relative moves. If the end position is: empty -> add & continue loop; friendly piece -> break extension loop;
   * enemy piece -> add & break extension loop.
   *
   * @param directions  double array of directions to be checked
   */
  static Collection<ChessMove> generateDirectionalMoves(ChessBoard board, ChessPosition position, int[][] directions) {

    Collection<ChessMove> moves = new HashSet<>();

    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      int i = 1;

      while (true) {
        ChessMove move = new ChessMove(position, i * rowOffset, i * colOffset);

        if (!(move.moveIsWithinBounds() && board.landsOnEmpty(move))) {
          if (board.landsOnEnemy(move)) {
            // Ends with enemy
            moves.add(move);
          }
          // Ends with enemy or friend
          break;
        }
        // Ends empty
        moves.add(move);
        i++;
      }
    }

    return moves;
  }


  /**
   * Generate all possible discrete moves for the given directions (kings and knights).
   *
   * @param board       chess board
   * @param position    position on given chess board
   * @param directions  provided array of directions
   */
  static Collection<ChessMove> generateDiscreteMoves(ChessBoard board, ChessPosition position, int[][] directions) {
    Collection<ChessMove> moves = new HashSet<>();

    for (int[] direction : directions) {
      int rowOffset = direction[0];
      int colOffset = direction[1];

      ChessMove move = new ChessMove(position, rowOffset, colOffset);
      if (board.validateMove(move)) {
        moves.add(move);
      }
    }

    return moves;
  }
}


/**
 * Generate and return possible moves for the KING found at given position on the given board.
 */
interface King extends PieceMovement {
  static Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position) {
    return PieceMovement.generateDiscreteMoves(board, position, ChessConstants.ALL_DIRECTIONS);
  }
}


/**
 * Generate and return possible moves for the QUEEN found at given position on the given board.
 */
interface Queen extends PieceMovement {
  static Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position) {
    return PieceMovement.generateDirectionalMoves(board, position, ChessConstants.ALL_DIRECTIONS);
  }
}


/**
 * Generate and return possible moves for the ROOK found at given position on the given board.
 */
interface Rook extends PieceMovement {
  static Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position) {
    return PieceMovement.generateDirectionalMoves(board, position, ChessConstants.ROOK_DIRECTIONS);
  }
}


/**
 * Generate and return possible moves for the BISHOP found at given position on the given board.
 */
interface Bishop extends PieceMovement {
  static Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position) {
    return PieceMovement.generateDirectionalMoves(board, position, ChessConstants.BISHOP_DIRECTIONS);
  }
}


/**
 * Generate and return possible moves for the KNIGHT found at given position on the given board.
 */
interface Knight extends PieceMovement {
  static Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position) {
    return PieceMovement.generateDiscreteMoves(board, position, ChessConstants.KNIGHT_DIRECTIONS);
  }
}


/**
 * Generate and return possible moves for the PAWN found at given position on the given board.
 */
interface Pawn extends PieceMovement {

  /**
   * Generates a pawn's moves.
   *
   * @param board     chess board
   * @param position  position on given chess board which contains a pawn
   * @return          Set of all possible moves for the pawn at 'position'
   */
  static Collection<ChessMove> generateMoves(ChessBoard board, ChessPosition position) {
    Collection<ChessMove> moves = new HashSet<>();

    ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

    boolean pawnIsWhite = (color == WHITE);
    int direction  = pawnIsWhite ? 1 : -1;
    boolean onStartRow = (pawnIsWhite ? 2 : 7) == position.getRow();
    boolean onEndRow = (pawnIsWhite ? 7 : 2) == position.getRow();

    if (onEndRow) {
      moves.addAll(generatePromotionMoves(board, position, direction));
    } else {
      moves.addAll(generateNonPromotionMoves(board, position, direction, onStartRow));
    }

    return moves;
  }


  /**
   * Generate pawn moves for a pawn that isn't about to promote.
   *
   * @param board       chess board
   * @param position    position on the chess board
   * @param direction   the direction in which the pawn is advancing
   * @param onStartRow  if the pawn is found on the start row
   * @return            the set of possible moves for the pawn
   */
  private static Collection<ChessMove> generateNonPromotionMoves(ChessBoard board, ChessPosition position, int direction, boolean onStartRow) {
    Collection<ChessMove> moves = new HashSet<>();

    ChessMove oneSquareMove = new ChessMove(position, direction, 0);

    if (board.validateMove(oneSquareMove)) {
      moves.add(oneSquareMove);

      ChessMove doubleMove = new ChessMove(position, 2 * direction, 0);
      if (onStartRow && board.validateMove(doubleMove)) {
        moves.add(doubleMove);
      }
    }

    moves.addAll(generateSideMoves(board, position, direction));
    return moves;
  }


  /**
   * Generate the possible side moves for a pawn.
   *
   * @param board       chess board
   * @param position    position on the chess board
   * @param direction   the direction in which the pawn is advancing
   * @return            the set of possible moves for the pawn
   */
  private static Collection<ChessMove> generateSideMoves(ChessBoard board, ChessPosition position, int direction) {
    Collection<ChessMove> moves = new HashSet<>();

    for (int offset : new int[]{-1, 1}) {
      ChessMove sideMove = new ChessMove(position, direction, offset);

      if (board.validateMove(sideMove)) {
        moves.add(sideMove);
      }
    }

    return moves;
  }


  /**
   * Generate pawn moves for a pawn that is about to promote.
   *
   * @param board       chess board
   * @param position    position on the chess board
   * @param direction   the direction in which the pawn is advancing
   * @return            the set of possible moves for the pawn
   */
  private static Collection<ChessMove> generatePromotionMoves(ChessBoard board, ChessPosition position, int direction) {
    Collection<ChessMove> moves = new HashSet<>();

    for (int offset : new int[]{-1, 0, 1}) {
      ChessMove move = new ChessMove(position, direction, offset);
      if (board.validateMove(move)) {
        for (ChessPiece.PieceType piece : ChessConstants.PROMOTION_TYPES) {
          moves.add(new ChessMove(position, direction, offset, piece));
        }
      }
    }

    return moves;
  }
}
