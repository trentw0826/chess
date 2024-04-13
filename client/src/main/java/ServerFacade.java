import command.CommandException;
import exception.ResponseException;
import facade.HttpCommunicator;
import facade.ServerMessageObserver;
import facade.WSCommunicator;
import model.GameData;
import playerColor.PlayerColor;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;

import java.util.ArrayList;

public class ServerFacade {
  private static final int NO_GAME = -1;
  private final HttpCommunicator httpCommunicator;
  private final ServerMessageObserver serverMessageObserver;
  private final ArrayList<GameData> currAvailableGames;
  private final String serverUrl;
  private int currGameId;
  private String currAuthToken;


  public ServerFacade(String serverUrl, ServerMessageObserver serverMessageObserver) {
    this.httpCommunicator = new HttpCommunicator(serverUrl);
    this.serverMessageObserver = serverMessageObserver;
    this.currAvailableGames = new ArrayList<>();
    this.serverUrl = serverUrl;
    resetCurrAuth();
  }


  public String resign() throws ResponseException {
    //TODO do I need to create new WsCommunicators for every usage?
    WSCommunicator webSocketCommunicator = new WSCommunicator(serverUrl, serverMessageObserver);
    webSocketCommunicator.resign(currGameId, currAuthToken);
    return "you've resigned!";
  }

  public String leave() throws ResponseException {
    WSCommunicator webSocketCommunicator = new WSCommunicator(serverUrl, serverMessageObserver);
    webSocketCommunicator.leave(currGameId, currAuthToken);
    currGameId = NO_GAME;
    return "you've left your game";
  }


  public String makeMove(String[] userInputArr) throws ResponseException {
    //FIXME fix makeMove logic
//    WSCommunicator webSocketCommunicator = new WSCommunicator(serverUrl, serverMessageObserver);
//    webSocketCommunicator.makeMove(currGameId, currAuthToken, new ChessMove(new ChessPosition(2, 2), 2, 2));
//    return "";
    return null;
  }


  public String register(String[] userInput) throws ResponseException {
    RegisterResponse registerResponse = httpCommunicator.registerUser(userInput[1], userInput[2], userInput[3]);
    return String.format("user '%s' registered%n", registerResponse.getUsername());
  }


  public String login(String[] userInput) throws ResponseException {
    LoginResponse loginResponse = httpCommunicator.login(userInput[1], userInput[2]);
    currAuthToken = loginResponse.getAuthToken();
    return "welcome!";
  }


  public String join(String[] userInput) throws ResponseException, CommandException {
    int localGameNum = Integer.parseInt(userInput[1]);
    int gameId = getGameIdFromLocal(localGameNum);

    PlayerColor playerColor = getPlayerColorFromString(userInput);

    httpCommunicator.joinPlayer(gameId, currAuthToken, playerColor);

    WSCommunicator webSocketCommunicator = new WSCommunicator(serverUrl, serverMessageObserver);
    webSocketCommunicator.joinPlayer(gameId, currAuthToken, playerColor);

    currGameId = gameId;
    return String.format("you've joined game #%d as %s", localGameNum, playerColor);
  }


  public String observe(String[] userInput) throws ResponseException, CommandException {
    int localGameNum = Integer.parseInt(userInput[1]);
    int gameId = getGameIdFromLocal(localGameNum);

    httpCommunicator.joinPlayer(gameId, currAuthToken, null);

    WSCommunicator webSocketCommunicator = new WSCommunicator(serverUrl, serverMessageObserver);
    webSocketCommunicator.joinObserver(gameId, currAuthToken);

    currGameId = gameId;
    return String.format("you're observing game #%d", localGameNum);
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

  private static PlayerColor getPlayerColorFromString(String[] userInput) throws CommandException {
    PlayerColor playerColor;
    try {
      playerColor = PlayerColor.valueOf(userInput[2].toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new CommandException("Make sure your color is 'white' or 'black'");
    }
    return playerColor;
  }

  private int getGameIdFromLocal(int localGameNum) throws CommandException {
    int gameId;
    if (localGameNum > 0 && localGameNum <= currAvailableGames.size()) {
      gameId = currAvailableGames.get(localGameNum - 1).getGameID();
    }
    else {
      throw new CommandException(String.format("Game with id '%d' not locally listed ('list' for games)", localGameNum));
    }
    return gameId;
  }

  private void resetCurrAuth() {
    currAuthToken = null;
  }
}
