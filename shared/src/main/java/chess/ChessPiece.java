package chess;

import consoleDraw.consoleDraw;

import java.util.Collection;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

  private final ChessGame.TeamColor pieceColor;
  private final ChessPiece.PieceType pieceType;

  /**
   * ChessPiece class constructor.
   *
   * @param type       The piece type
   * @param pieceColor The color of the piece
   */
  public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
      this.pieceColor = pieceColor;
      this.pieceType = type;
  }


  /**
   * @return string representation of piece
   */
  @Override
  public String toString() {
    String str = null;

    //TODO update piecetype enum to contain these values rather than having to retrieve them
    if (pieceColor == WHITE) {
      switch (pieceType) {
        case KING -> str = consoleDraw.WHITE_KING;
        case QUEEN -> str = consoleDraw.WHITE_QUEEN;
        case KNIGHT -> str = consoleDraw.WHITE_KNIGHT;
        case BISHOP -> str = consoleDraw.WHITE_BISHOP;
        case ROOK -> str = consoleDraw.WHITE_ROOK;
        case PAWN -> str = consoleDraw.WHITE_PAWN;
        default -> throw new IllegalStateException();
      }
    }
    else if (pieceColor == BLACK) {
      switch (pieceType) {
        case KING -> str = consoleDraw.BLACK_KING;
        case QUEEN -> str = consoleDraw.BLACK_QUEEN;
        case KNIGHT -> str = consoleDraw.BLACK_KNIGHT;
        case BISHOP -> str = consoleDraw.BLACK_BISHOP;
        case ROOK -> str = consoleDraw.BLACK_ROOK;
        case PAWN -> str = consoleDraw.BLACK_PAWN;
        default -> throw new IllegalStateException();
      }
    }
    return str;
  }


  /**
   * The various different chess piece options
   */
  public enum PieceType {
    KING,
    QUEEN,
    BISHOP,
    KNIGHT,
    ROOK,
    PAWN,
  }


  /**
   * @param o other ChessPiece object
   * @return if their attributes are equal
   */
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ChessPiece that=(ChessPiece) o;
      return pieceColor == that.pieceColor && pieceType == that.pieceType;
  }


  /**
   * @return hashCode based on class attributes
   */
  @Override
  public int hashCode() {
      return Objects.hash(pieceColor, pieceType);
  }


  /**
   * Overwritten clone  method.
   *
   * @return  clone of the current ChessPiece object
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
      return super.clone();
  }


  /**
   * @return Which team this chess piece belongs to
   */
  public ChessGame.TeamColor getTeamColor() {
      return pieceColor;
  }


  /**
   * @return which type of chess piece this piece is
   */
  public PieceType getPieceType() {
      return pieceType;
  }


  /**
   * Calculates all the positions a chess piece can move to.
   * Does not take into account moves that are illegal due to leaving the king in
   * danger.
   *
   * @return Collection of valid moves
   */
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
    return PieceMovement.getPossibleMoves(board, position);
  }
}
