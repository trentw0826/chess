package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private static final String MOVE_REGEX = "([a-h][1-8][a-h][1-8])(=[QRNB])?";

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
     * Constructs a ChessMove using a string
     *
     * @param chessMoveStr  string that matches 'MOVE_REGEX'
     */
    public ChessMove(String chessMoveStr) {
        if (!chessMoveStr.matches(MOVE_REGEX)) {
            throw new IllegalArgumentException("Bad string passed to ChessMove constructor");
        }
        else {
            String startPositionStr = chessMoveStr.substring(0, 2);
            String endPositionStr = chessMoveStr.substring(2, 4);
            char promotionPieceChar = chessMoveStr.length() >= 6 ? chessMoveStr.charAt(6) : '0';


            this.startPosition = new ChessPosition(startPositionStr);
            this.endPosition = new ChessPosition(endPositionStr);

            ChessPiece.PieceType assignedPromotion = null;
            for (var promotionType : ChessConstants.PROMOTION_TYPES) {
                if (promotionType.getPieceChar() == promotionPieceChar) {
                    assignedPromotion = promotionType;
                    break;
                }
            }

            this.promotionPiece = assignedPromotion;
        }
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
     * @return  promotionPiece getter
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    /**
     * @return  if chessMove object has a promotion piece
     */
    public boolean hasPromotionPiece() {
        return promotionPiece != null;
    }

    /**
     * @return string representation of ChessMove object
     */
    @Override
    public String toString() {
        //TODO Update tostring to be more along the lines of (a1->a2 = Q)
        return startPosition + "->" + endPosition;
    }


    /**
     * @return  If both the start and end positions are found on the board
     */
    public boolean moveIsWithinBounds() {
        return (endPosition.positionIsWithinBounds() && startPosition.positionIsWithinBounds());
    }
}
