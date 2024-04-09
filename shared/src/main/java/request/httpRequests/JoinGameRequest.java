package request.httpRequests;

import playerColor.PlayerColor;

/**
 * Holds required information for a service join game request.
 *
 * @param authToken   auth token
 * @param playerColor player color
 * @param gameID      game ID
 */
public record JoinGameRequest(String authToken, PlayerColor playerColor, Integer gameID) implements ServiceRequest {}
