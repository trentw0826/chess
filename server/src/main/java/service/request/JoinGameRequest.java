package service.request;

/**
 * Holds required information for a service join game request.
 *
 * @param authToken   auth token
 * @param playerColor player color
 * @param gameID      game ID
 */
public record JoinGameRequest(String authToken, String playerColor, Integer gameID) implements ServiceRequest {}
