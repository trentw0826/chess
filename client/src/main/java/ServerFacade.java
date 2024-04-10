import command.CommandException;
import exception.ResponseException;
import facade.HttpCommunicator;
import facade.ServerMessageObserver;
import facade.WsCommunicator;
import model.GameData;
import playerColor.PlayerColor;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;

import java.util.ArrayList;

public class ServerFacade {
  private final HttpCommunicator httpCommunicator;
  private final ServerMessageObserver serverMessageObserver;
  private final ArrayList<GameData> currAvailableGames;
  private final String serverUrl;
  private String currAuthToken;


  public ServerFacade(String serverUrl, ServerMessageObserver serverMessageObserver) {
    this.httpCommunicator = new HttpCommunicator(serverUrl);
    this.serverMessageObserver = serverMessageObserver;
    this.currAvailableGames = new ArrayList<>();
    this.serverUrl = serverUrl;
    resetCurrAuth();
  }

  public String redraw() {
    //TODO Implement
    return null;
  }

  public String resign() {
    //TODO Implement
    return null;
  }

  public String leave() {
    //TODO Implement
    return null;
  }

  public String makeMove(String[] userInputArr) {
    //TODO Implement
    return null;
  }

  public String highlight(String[] userInputArr) {
    //TODO Implement
    return null;
  }

  public String register(String[] userInput) throws ResponseException {
    RegisterResponse registerResponse = httpCommunicator.registerUser(userInput[1], userInput[2], userInput[3]);
    return String.format("user '%s' registered%n", registerResponse.getUsername());
  }


  public String login(String[] userInput) throws ResponseException {
    LoginResponse loginResponse = httpCommunicator.login(userInput[1], userInput[2]);
    currAuthToken = loginResponse.getAuthToken();
    return String.format("user '%s' logged in%n", loginResponse.getUsername());
  }


  public String join(String[] userInput) throws ResponseException, CommandException {
    int localGameNum = Integer.parseInt(userInput[1]);
    int gameId = 0;
    if (localGameNum > 0 && localGameNum <= currAvailableGames.size()) {
      gameId = currAvailableGames.get(localGameNum - 1).getGameID();
    }
    else {
      throw new CommandException(String.format("Game with id '%d' not locally listed ('list' for games)", localGameNum));
    }

    PlayerColor playerColor;
    try {
      playerColor = PlayerColor.valueOf(userInput[2].toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new CommandException("Make sure your color is 'white' or 'black'");
    }

    httpCommunicator.joinGame(gameId, currAuthToken, playerColor);

    WsCommunicator wsCommunicator = new WsCommunicator(serverUrl, serverMessageObserver);
    wsCommunicator.joinPlayer(gameId, currAuthToken, playerColor);

    return String.format("you've joined game #%d as %s", localGameNum, playerColor);
  }

  public String observe(String[] userInput) throws ResponseException {
    //TODO Implement observe logic
    return null;
  }


  public String create(String[] userInput) throws ResponseException {
    CreateGameResponse createGameResponse = httpCommunicator.createGame(userInput[1], currAuthToken);
    return String.format("'%s' created", userInput[1]);
  }


  public String list() throws ResponseException {
    ListGamesResponse listGamesResponse = httpCommunicator.listGames(currAuthToken);

    StringBuilder sb = new StringBuilder();
    currAvailableGames.clear();
    currAvailableGames.addAll(listGamesResponse.getGames());
    if (currAvailableGames.isEmpty()) {
      sb.append("no available games :(");
    }
    else {
      int i = 0;
      for (GameData gameData : currAvailableGames) {
        i++;
        sb.append(String.format("%d: %s%n", i, gameData.headerStr()));
      }
    }
    return sb.toString();
  }


  public String logout() throws ResponseException {
    httpCommunicator.logout(currAuthToken);
    resetCurrAuth();
    return "logged out successfully.";
  }

  private void resetCurrAuth() {
    currAuthToken = null;
  }
}
