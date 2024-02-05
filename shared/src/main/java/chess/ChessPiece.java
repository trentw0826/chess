package chess;

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
  public ChessPiece(PieceType type, ChessGame.TeamColor pieceColor) {
      this.pieceColor = pieceColor;
      this.pieceType = type;
  }


  /**
   * @return string representation of piece
   */
  @Override
  public String toString() {
    char c = ChessConstants.PIECE_TYPE_TO_CHAR.get(pieceType);
    return String.valueOf((pieceColor == BLACK) ? c : Character.toUpperCase(c));
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
