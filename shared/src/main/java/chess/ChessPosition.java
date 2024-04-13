package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    /**
     * Constructs a ChessPosition objects.
     *
     * @param row   desired row position
     * @param col   desired col position
     */
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }


    /**
     * Constructor that takes string representations of positions (i.e. f5)
     * @param positionStr   string of the form [a-hA-H][1-8]
     */
    public ChessPosition(String positionStr) {
        if (!positionStr.matches("[a-hA-H][1-8]")) {
            throw new IllegalArgumentException("Bad string passed to ChessPosition constructor");
        }

        char colChar = positionStr.charAt(0);
        this.row = Character.getNumericValue(positionStr.charAt(1));
        this.col = colChar - 'a' + 1;
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


    /**
     * @return string representation of ChessPosition object
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ')';
    }


    /**
     * @return if both the row and the col are within the 8x8 bounds of the board
     */
    //TODO extract magic numbers
    public boolean positionIsWithinBounds() {
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
