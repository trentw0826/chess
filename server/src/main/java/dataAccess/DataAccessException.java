package dataAccess;

import service.response.ServiceResponse;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(ServiceResponse.ERROR_MESSAGE message) {
        super(ServiceResponse.errorMessageToString.get(message));
    }
}
