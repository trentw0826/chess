package model;


import java.util.UUID;

/**
 * Holds data necessary to authorize a user.
 *
 * @param authToken the user's authentication token
 * @param username  the user's username
 */
public record AuthData(AuthToken authToken, String username) {}

