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

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this(startPosition, endPosition,null);
    }

    public ChessMove(ChessPosition startPosition, int x, int y) {
        this(startPosition, startPosition.getRelativePosition(x, y), null);

    }

    /**
     * @param o other ChessMove object
     * @return  if both objects' attributes are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
     * @return  ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return  ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public String toString() {
        return startPosition + "->" + endPosition;
    }

    /**
     * @return  If both the start and end positions are found on the board
     */
    public boolean moveIsOnBoard() {
        return (endPosition.positionIsOnBoard() && startPosition.positionIsOnBoard());
    }
}
