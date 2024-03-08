package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    // TODO why are some of these messages coming back in all caps when called with this constructor?
    public DataAccessException(ErrorMessages errorMessage) {
        super(errorMessage.message());
    }

    public DataAccessException(String errorMessage) {
        super(errorMessage);
    }

    public enum ErrorMessages {
        BAD_REQUEST("Error: bad request"),
        UNAUTHORIZED("Error: unauthorized"),
        ALREADY_TAKEN("Error: already taken");

        private final String message;

        ErrorMessages(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }
    }
}
