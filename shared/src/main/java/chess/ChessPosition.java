package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Setter for row value
     * @param row New desired row value
     */
    public void setRow(int row) {
        this.row=row;
    }

    /**
     * Setter for col value
     * @param col New desired col value
     */
    public void setCol(int col) {
        this.col=col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    /**
     * @return if both the row and the col are within the 8x8 bounds of the board
     */
    public boolean positionIsOnBoard() {
        return (0 <= row && row < 8 && 0 <= col && col < 8);
    }
}
