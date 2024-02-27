package service.request;

/**
 * Holds required information for a service create game request.
 *
 * @param gameName  game name
 * @param authToken valid auth token
 */
public record CreateGameRequest(String gameName, String authToken) implements ServiceRequest {}
