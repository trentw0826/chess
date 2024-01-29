package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException() {
        super("Attempted to make invalid move!");
    }

    public InvalidMoveException(String message) {
        super(message);
    }
}
