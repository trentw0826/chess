package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;


    /**
     * Constructs a ChessMove object with promotion piece specified.
     * @param startPosition     desired start position
     * @param endPosition       desired end position
     * @param promotionPiece    desired promotion piece
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }


    /**
     * Constructs a ChessMove object without promotion piece specified (null).
     * @param startPosition     desired start position
     * @param endPosition       desired end position
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this(startPosition, endPosition,null);
    }


    /**
     * Constructs a ChessMove object by end coordinates, promotion piece unspecified (null).
     * @param startPosition desired start position
     * @param x             desired ending row
     * @param y             desired ending column
     */
    public ChessMove(ChessPosition startPosition, int x, int y) {
        this(startPosition, x, y, null);
    }


    /**
     * Constructs a ChessMove object by end coordinates, promotion piece specified@param startPosition desired start position.
     * @param x             desired ending row
     * @param y             desired ending column
     * @param piece         desired promotion piece
     */
    public ChessMove(ChessPosition startPosition, int x, int y, ChessPiece.PieceType piece) {
        this(startPosition, startPosition.getRelativePosition(x, y), piece);
    }


    /**
     * @param o other ChessMove object
     * @return  if both objects' attributes are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false   ;
        ChessMove chessMove=(ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }


    /**
     * @return  hashCode based on attributes of the ChessMove class
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }


    /**
     * @return  startPosition getter
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }


    /**
     * @return  endPosition getter
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }


    /**
     * @return string representation of ChessMove object
     */
    @Override
    public String toString() {
        return startPosition + "->" + endPosition;
    }


    /**
     * @return  If both the start and end positions are found on the board
     */
    public boolean moveIsWithinBounds() {
        return (endPosition.positionIsWithinBounds() && startPosition.positionIsWithinBounds());
    }
}
