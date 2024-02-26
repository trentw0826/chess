package service.request;

/**
 * Holds required information for a service logout request
 * @param authToken auth token
 */
public record LogoutRequest(String authToken) implements ServiceRequest {}
