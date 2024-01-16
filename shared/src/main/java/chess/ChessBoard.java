package chess;



/**
 * A chessboard that can hold and rearrange chess pieces.
 */
public class ChessBoard {
    public static final int LENGTH = 8; //TODO: find where this should go (constants class?)

    private ChessPiece[][] board;

    /**
     * Constructor
     */
    public ChessBoard() {
        board = new ChessPiece[LENGTH][LENGTH];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position Where to add the piece to
     * @param piece    The piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
