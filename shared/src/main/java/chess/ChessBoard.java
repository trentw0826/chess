package chess;


import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 */
public class ChessBoard {
    public static final int LENGTH = 8; //TODO: find where this should go (constants class?) + replace all instances of '8'
//    private ChessPiece[][] startingBoard;

    private ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[LENGTH][LENGTH];
    }

//    /**
//     * Populate the starting board with ChessPiece objects
//     */
//    private void populateStartingBoard() {
//        final String[][] startingBoardString = {
//                {"WR♖", "WN♘", "WB♗", "WK♔", "WQ♕", "WB♗", "WN♘", "WR♖"},
//                {"WP♙", "WP♙", "WP♙", "WP♙", "WP♙", "WP♙", "WP♙", "WP♙"},
//                {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
//                {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
//                {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
//                {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
//                {"BP♟", "BP♟", "BP♟", "BP♟", "BP♟", "BP♟", "BP♟", "BP♟"},
//                {"BR♜", "BN♞", "BB♝", "BK♚", "BQ♛", "BB♝", "BN♞", "BR♜"},
//        };
//    }

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
                {new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)},
                {new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)},
                {new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK)},
        };
    }
}
