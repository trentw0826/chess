package dataAccess;

import service.ServiceConstants;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(ServiceConstants.ErrorMessages errorMessage) {
        super(errorMessage.message());
    }
}
