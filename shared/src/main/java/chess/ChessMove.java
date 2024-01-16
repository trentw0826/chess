package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Setter for private asset startPosition
     * @param startPosition Desired startPosition
     */
    public void setStartPosition(ChessPosition startPosition) {
        this.startPosition=startPosition;
    }

    /**
     * Setter for private asset endPosition
     * @param endPosition Desired endPosition
     */
    public void setEndPosition(ChessPosition endPosition) {
        this.endPosition=endPosition;
    }

    /**
     * Setter for private asset promotionPiece
     * @param promotionPiece Desired promotionPiece
     */
    public void setPromotionPiece(ChessPiece.PieceType promotionPiece) {
        this.promotionPiece=promotionPiece;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        throw new RuntimeException("Not implemented");
    }
}
