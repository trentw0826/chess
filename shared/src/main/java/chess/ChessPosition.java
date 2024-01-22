package chess;

import java.util.Objects;

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
     * @param o other ChessPosition object
     * @return if their attributes are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that=(ChessPosition) o;
        return row == that.row && col == that.col;
    }

    /**
     * @return hashCode based on class attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Setter for row value
     * @param row New desired row value
     */
    public void setRow(int row) { this.row = row; }

    /**
     * Setter for col value
     * @param col New desired col value
     */
    public void setCol(int col) {
        this.col = col;
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
    public int getColumn() { return col; }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ')';
    }

    /**
     * @return if both the row and the col are within the 8x8 bounds of the board
     */
    public boolean positionIsOnBoard() {
        return (0 < row && row <= 8 && 0 < col && col <= 8);
    }

    /**
     * @param xVal  the x value to update the row
     * @param yVal  the y value to update the column
     * @return      a ChessPosition updated relatively by 'xVal' and 'yVal'
     */
    public ChessPosition getRelativePosition(int xVal, int yVal) {
        return new ChessPosition(row + xVal, col + yVal);
    }
}
