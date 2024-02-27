package dataAccess;

import service.ServiceConstants;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private final ServiceConstants.ERROR_MESSAGES message;

    public DataAccessException(ServiceConstants.ERROR_MESSAGES errorMessage) {
        super(errorMessage.message());
        message = errorMessage;
    }

    public ServiceConstants.ERROR_MESSAGES getEnumMessage() {
        return message;
    }
}
