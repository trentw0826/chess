package model;

import chess.ChessGame;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds data representing a chess game on the server.
 */
public class GameData implements DataModel<Integer> {

  private Integer gameID;
  private String whiteUsername;
  private String blackUsername;
  private final Set<String> observers = new HashSet<>();
  private final String gameName;
  private ChessGame game;
  private boolean gameActive;

  public GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, boolean gameActive) {
    this.gameID = gameID;
    this.whiteUsername = whiteUsername;
    this.blackUsername = blackUsername;
    this.gameName = gameName;
    this.game = game;
    this.gameActive = gameActive;
  }

  public GameData(String gameName) {
    this(null, null, null, gameName, new ChessGame(), true);
  }

  public String getWhiteUsername() {
    return whiteUsername;
  }

  public void setWhiteUsername(String whiteUsername) {
    this.whiteUsername = whiteUsername;
  }

  public String getBlackUsername() {
    return blackUsername;
  }

  public void setBlackUsername(String blackUsername) {
    this.blackUsername = blackUsername;
  }

  public Integer getGameID() {
    return gameID;
  }

  public void setGameID(Integer gameID) {
    this.gameID = gameID;
  }

  public String getGameName() {
    return gameName;
  }

  public ChessGame getGame() {
    return game;
  }

  public void addObserver(String newObserverUsername) {
    observers.add(newObserverUsername);
  }

  public boolean isGameActive() {
    return gameActive;
  }

  @Override
  public boolean hasNullFields() {
    return (gameName == null || game == null);
  }

  @Override
  public Integer generateKey() {
    return gameID;
  }

  public String headerStr() {
    String playerStatus;
    if (whiteUsername == null && blackUsername == null) {
      playerStatus = "no current players";
    }
    else {
      if ((whiteUsername == null)) {
        playerStatus = String.format("[BLACK] \"%s\" is waiting for an opponent...", blackUsername);
      }
      else {
        if (blackUsername == null)
          playerStatus = String.format("[WHITE] \"%s\" is waiting for an opponent...", whiteUsername);
        else {
          playerStatus = String.format("[WHITE] \"%s\" versus [BLACK] \"%s\"", whiteUsername, blackUsername);
        }
      }
    }

    String gameActiveStr = gameActive ? "" : " [finished]";

    return String.format("[\"%s\"] [%s]%s", gameName, playerStatus, gameActiveStr);
  }
}
