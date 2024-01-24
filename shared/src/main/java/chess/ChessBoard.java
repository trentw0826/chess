package chess;


import java.util.Arrays;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 */
public class ChessBoard {
    public static final int LENGTH = 8;

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[LENGTH][LENGTH];
    }

    /**
     * @param o other ChessBoard object
     * @return true if the boards are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that=(ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    /**
     * @return deepHashCode of board attribute
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * @return deepToString of board attribute
     */
    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }

    /**
     * Checks if a position is empty
     *
     * @param position a position to check for emptiness
     * @return if the piece at 'position' is null
     */
    public boolean positionIsEmpty(ChessPosition position) {
        return (position.positionIsOnBoard() && board[position.getRow() - 1][position.getColumn() - 1] == null);
    }

    /**
     * Checks if the ending position of a move is empty
     * @param move a move to check for emptiness
     * @return if the ending square is blank
     */
    public boolean endPositionEmpty(ChessMove move) {
        return positionIsEmpty(move.getEndPosition());
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position Where to add the piece to
     * @param piece    The piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (!position.positionIsOnBoard()) {
            return null;
        }
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * @param move selected ChessMove on the board
     * @return ChessPiece found at the start position of 'move'
     */
    public ChessPiece getPiece(ChessMove move) {
        return (getPiece(move.getStartPosition()));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[][]{
                {new ChessPiece(WHITE, ROOK), new ChessPiece(WHITE, KNIGHT), new ChessPiece(WHITE, BISHOP), new ChessPiece(WHITE, QUEEN), new ChessPiece(WHITE, KING), new ChessPiece(WHITE, BISHOP), new ChessPiece(WHITE, KNIGHT), new ChessPiece(WHITE, ROOK)},
                {new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN), new ChessPiece(WHITE, PAWN)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK, PAWN), new ChessPiece(BLACK,PAWN)},
                {new ChessPiece(BLACK, ROOK), new ChessPiece(BLACK, KNIGHT), new ChessPiece(BLACK, BISHOP), new ChessPiece(BLACK, QUEEN), new ChessPiece(BLACK, KING), new ChessPiece(BLACK,BISHOP), new ChessPiece(BLACK, KNIGHT), new ChessPiece(BLACK, ROOK)},
        };
    }
}
