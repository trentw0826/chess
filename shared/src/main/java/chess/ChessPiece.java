package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
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
     * Setter for private variable pieceColor
     * @param pieceColor The new desired pieceColor
     */
    public void setPieceColor(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    /**
     * Setter for private variable pieceType
     * @param pieceType The new desired pieceType
     */
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
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
        return pieceMoves.pieceMoves();
    }
}
