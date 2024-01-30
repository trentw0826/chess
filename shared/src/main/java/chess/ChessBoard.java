package chess;

import java.util.*;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 */
public class ChessBoard implements Cloneable {

    private ChessPiece[][] board;

    /**
     * Constructs ChessBoard object, a blank double array of size 8x8.
     */
    public ChessBoard() {
        board = new ChessPiece[ChessConstants.BOARD_SIZE][ChessConstants.BOARD_SIZE];
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
        StringBuilder str = new StringBuilder();
        for (int i = 8; i >= 1; i--) {
            ChessPiece[] row = board[i - 1];
            for (ChessPiece piece : row) {
                String pieceStr = (piece == null) ? " " : piece.toString();
                str.append("|").append(pieceStr);
            }
            str.append("|\n");
        }
        return str.toString();
    }


    /**
     * Overwritten clone method.
     *
     * @return  a deep copy of the ChessBoard object
     * @throws  CloneNotSupportedException if cloning is not supported
     */
    @Override
    public ChessBoard clone() throws CloneNotSupportedException {
        ChessBoard clonedBoard = (ChessBoard) super.clone();

        // Deep copy of the ChessPiece objects
        clonedBoard.board = new ChessPiece[ChessConstants.BOARD_SIZE][ChessConstants.BOARD_SIZE];
        for (int i = 0; i < ChessConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < ChessConstants.BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    clonedBoard.board[i][j] = (ChessPiece) board[i][j].clone();
                }
            }
        }

        return clonedBoard;
    }


    /**
     * Checks if the ending position of a move is empty.
     *
     * @param move a move to check for emptiness
     * @return if the ending square is blank
     */
    public boolean landsOnEmpty(ChessMove move) {
        ChessPosition endPosition = move.getEndPosition();
        return (endPosition.positionIsWithinBounds() && board[endPosition.getRow() - 1][endPosition.getColumn() - 1] == null);
    }


    /**
     * Return if the starting piece and ending piece differ in color (if there is an ending piece).
     *
     * @param move  move to be checked
     * @return      if the ending position contains an enemy piece
     */
    public boolean landsOnEnemy(ChessMove move) {
        ChessPiece startPiece = getPiece(move.getStartPosition());
        ChessPiece endPiece = getPiece(move.getEndPosition());

        // The end piece exists and is an enemy
        return endPiece != null && endPiece.getTeamColor() != startPiece.getTeamColor();
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
        if (!position.positionIsWithinBounds()) {
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
     * @return  'board' attribute
     */
    public ChessPiece[][] getBoard() {
        return board;
    }

    /**
     * Validates a move based on the board and a given current turn.
     *
     * @param move      the move to be checked
     * @param currTurn  current game turn
     * @return          if the move is valid
     */
    protected boolean validateMove(ChessMove move, ChessGame.TeamColor currTurn) {
        ChessPiece piece = getPiece(move);
        if (piece.getTeamColor() != currTurn) {
            return false;
        }
        return validateMove(move);
    }

    /**
     * Validates a move without checking the turn.
     *
     * @param move  move to be checked
     * @return      if the move is valid
     */
    protected boolean validateMove(ChessMove move) {
        ChessPiece piece = getPiece(move);
        if (piece.getPieceType() == PAWN) {
            return validatePawnMove(move);
        } else {
            // The end position is on the board and is either empty or occupied by an enemy piece
            return move.moveIsWithinBounds() && (landsOnEnemy(move) || landsOnEmpty(move));
        }
    }


    /**
     * Validates a pawn move.
     * A pawn move is valid if: it moves once to an empty square, it moves twice to an empty square
     * (passing through an empty square), it moves up and to the side to an enemy-occupied square
     *
     * @param move  ChessMove object to be checked
     * @return      If pawn move is valid
     */
    protected boolean validatePawnMove(ChessMove move) {

        int direction = (getPiece(move).getTeamColor() == WHITE) ? 1 : -1;

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        int rowDifference = Math.abs(endPosition.getRow() - startPosition.getRow());
        int colDifference = Math.abs(endPosition.getColumn() - startPosition.getColumn());

        if (rowDifference == 1) {
            if (colDifference == 1) {       // Sideways capture
                return landsOnEnemy(move);
            }
            else if (colDifference == 0) {  // Single forward push
                return landsOnEmpty(move);
            }
            else {                          // Invalid colDifference
                return false;
            }
        }
        else if (rowDifference == 2) {    // Double forward push
            ChessMove doubleMove = new ChessMove(move.getStartPosition(), 2 * direction, 0);
            return landsOnEmpty(move) && landsOnEmpty(doubleMove);
        }
        return false;                     // Invalid pawn move
    }


    /**
     * Adds an empty piece to the board at the given position.
     *
     * @param position  position to be made empty
     */
    public void addEmptyPiece(ChessPosition position) {
        addPiece(position, null);
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
