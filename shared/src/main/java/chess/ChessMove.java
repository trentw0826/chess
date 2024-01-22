package chess;

import java.util.Objects;

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
     * Constructor creates ChessMove based on two relative numbers
     * @param startPosition     start position
     * @param x                 row offset
     * @param y                 column offset
     * @param promotionPiece    promotion piece
     */
    public ChessMove(ChessPosition startPosition, int x, int y, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = startPosition.getRelativePosition(x, y);
        this.promotionPiece = promotionPiece;
    }

    /**
     * @param o other ChessMove object
     * @return if both objects' attributes are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove=(ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    /**
     * @return hashCode based on attributes of the ChessMove class
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
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

    @Override
    public String toString() {
        return startPosition + "->" + endPosition;
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

    /**
     * @return  If both the start and end positions are found on the board
     */
    public boolean moveIsOnBoard() {
        return (endPosition.positionIsOnBoard() && startPosition.positionIsOnBoard());
    }
}
