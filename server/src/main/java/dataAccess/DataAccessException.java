package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{

    public DataAccessException(ErrorMessages errorMessage) {
        super(errorMessage.message());
    }

    public DataAccessException(String errorMessage) {
        super(errorMessage);
    }

    /*
     * Custom error messages defining the nature of data access exceptions
     */
    public enum ErrorMessages {
        BAD_REQUEST("bad request"),
        UNAUTHORIZED("unauthorized"),
        ALREADY_TAKEN("already taken");

        private final String message;

        ErrorMessages(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }
    }
}
