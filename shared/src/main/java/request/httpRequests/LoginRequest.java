package request.httpRequests;

/**
 * Holds required information for a service login request.
 *
 * @param username  username
 * @param password  password
 */
public record LoginRequest(String username, String password) implements ServiceRequest {}
