package chess;

import java.util.Arrays;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 */
public class ChessBoard {

    public static final int SIZE = 8;
    private ChessPiece[][] board;

    /**
     * Constructs ChessBoard object, a blank double array of size 8x8.
     */
    public ChessBoard() {
        board = new ChessPiece[SIZE][SIZE];
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
     * Checks if the ending position of a move is empty.
     *
     * @param move a move to check for emptiness
     * @return if the ending square is blank
     */
    public boolean endPositionIsEmpty(ChessMove move) {
        ChessPosition endPosition = move.getEndPosition();
        return (endPosition.positionIsOnBoard() && board[endPosition.getRow() - 1][endPosition.getColumn() - 1] == null);
    }


    /**
     * Return if the starting piece and ending piece differ in color (if there is an ending piece).
     *
     * @param move  move to be checked
     * @return      if the ending position contains an enemy piece
     */
    public boolean endPositionIsEnemy(ChessMove move) {
        ChessPiece startPiece = getPiece(move);
        ChessPiece endPiece = getPiece(move.getEndPosition());

        if (endPiece == null || startPiece == null) { return false; }

        return (endPiece.getTeamColor() != startPiece.getTeamColor());
    }


    /**
     * Adds a chess piece to the board.
     *
     * @param position where to add piece
     * @param piece    piece to be added
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }


    /**
     * Gets a chess piece on the chessboard.
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
     * Updates the chess board to the classic starting position.
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
