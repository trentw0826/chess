package model;

import java.util.UUID;

public record AuthToken(String authToken) {
  public AuthToken() {
    this(UUID.randomUUID().toString());
  }
}
