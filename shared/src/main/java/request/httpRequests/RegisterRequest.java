package request.httpRequests;

/**
 * Holds required information for a service register request.
 *
 * @param username  username
 * @param password  password
 * @param email     email
 */
public record RegisterRequest(String username, String password, String email) implements ServiceRequest {}
