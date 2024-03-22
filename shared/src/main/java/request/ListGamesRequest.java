package request;

/**
 * Holds required information for a service list games request.
 *
 * @param authToken auth token
 */
public record ListGamesRequest(String authToken) implements ServiceRequest {}
