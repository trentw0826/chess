package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final String[] chessStrings = {
            "WK♔", "WQ♕", "WR♖", "WB♗", "WN♘", "WP♙",
            "BK♚", "BQ♛", "BR♜", "BB♝", "BN♞", "BP♟",
            "   "
    };
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * ChessPiece class constructor
     *
     * @param pieceColor    The color of the piece
     * @param type          The piece type
     */
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
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

    @Override
    public String toString() {
        String str;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // White team
            switch (pieceType) {
                case KING -> str=chessStrings[0];
                case QUEEN -> str=chessStrings[1];
                case ROOK -> str=chessStrings[2];
                case BISHOP -> str=chessStrings[3];
                case KNIGHT -> str=chessStrings[4];
                case PAWN -> str=chessStrings[5];
                default -> throw new IllegalArgumentException("invalid pieceType referenced");
            }
        } else {
            // Black team
            switch (pieceType) {
                case KING -> str=chessStrings[6];
                case QUEEN -> str=chessStrings[7];
                case ROOK -> str=chessStrings[8];
                case BISHOP -> str=chessStrings[9];
                case KNIGHT -> str=chessStrings[10];
                case PAWN -> str=chessStrings[11];
                default -> throw new IllegalArgumentException("invalid pieceType referenced");
            }
        }
        return str;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessPiece.PieceType type = piece.pieceType;
        ChessGame.TeamColor teamColor = piece.pieceColor;
        PieceMovement pieceMoves;
        switch (type) {
            case KING -> pieceMoves = new King(board, myPosition, teamColor);
            case QUEEN -> pieceMoves = new Queen(board, myPosition, teamColor);
            case ROOK -> pieceMoves = new Rook(board, myPosition, teamColor);
            case BISHOP -> pieceMoves = new Bishop(board, myPosition, teamColor);
            case KNIGHT -> pieceMoves = new Knight(board, myPosition, teamColor);
            case PAWN -> pieceMoves = new Pawn(board, myPosition, teamColor);
            default -> throw new IllegalArgumentException("Invalid pieceType");
        }
        return pieceMoves.getPossibleMoves();
    }
}
