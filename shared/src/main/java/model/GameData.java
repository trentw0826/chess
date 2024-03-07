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

  public GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    this.gameID = gameID;
    this.whiteUsername = whiteUsername;
    this.blackUsername = blackUsername;
    this.gameName = gameName;
    this.game = game;
  }

  public GameData(String gameName) {
    this(null, null, null, gameName, null);
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

  public void initializeEmptyGame() {
    game = new ChessGame();
  }

  public void addObserver(String newObserverUsername) {
    observers.add(newObserverUsername);
  }

  @Override
  public boolean hasNullFields() {
    return (gameName == null || game == null);
  }

  @Override
  public Integer generateKey() {
    return gameID;
  }
}
