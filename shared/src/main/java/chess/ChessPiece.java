package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.EnumMap;

import static chess.ChessPiece.PieceType.*;
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

    // Mapping of enum piece type values to string representations
    private static final EnumMap<PieceType, String> whitePieceStrings = new EnumMap<>(PieceType.class);
    private static final EnumMap<PieceType, String> blackPieceStrings = new EnumMap<>(PieceType.class);
    static {
        whitePieceStrings.put(KING, "WK♔");
        whitePieceStrings.put(QUEEN, "WQ♕");
        whitePieceStrings.put(ROOK, "WR♖");
        whitePieceStrings.put(BISHOP, "WB♗");
        whitePieceStrings.put(KNIGHT, "WN♘");
        whitePieceStrings.put(PAWN, "WP♙");
        whitePieceStrings.put(NONE, "   ");
        blackPieceStrings.put(KING, "BK♚");
        blackPieceStrings.put(QUEEN, "BQ♛");
        blackPieceStrings.put(ROOK, "BR♜");
        blackPieceStrings.put(BISHOP, "BB♝");
        blackPieceStrings.put(KNIGHT, "BN♞");
        blackPieceStrings.put(PAWN, "BP♟");
        blackPieceStrings.put(NONE, "   ");
    }

    /**
     * ChessPiece class constructor.
     *
     * @param pieceColor    The color of the piece
     * @param type          The piece type
     */
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }


    /**
     * @return string representation of piece
     */
    @Override
    public String toString() {
        return (pieceColor == WHITE) ? whitePieceStrings.get(pieceType) : blackPieceStrings.get(pieceType);
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
        NONE
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
        return ChessRuleBook.getValidMoves(board, position);
    }
}
