package command;

import exception.ResponseException;
import facade.ServerMessageObserver;
import playerColor.PlayerColor;
import response.*;
import model.*;
import facade.HttpCommunicator;
import facade.WsCommunicator;

import java.util.ArrayList;

public class CommandProcessor {

  public static final String TERMINATE = "terminate";

  private final String serverUrl;
  private final HttpCommunicator httpCommunicator;
  private final ArrayList<GameData> currAvailableGames;
  private final ServerMessageObserver serverMessageObserver;
  private ClientState currState;
  private String currAuthToken;

  public CommandProcessor(String serverUrl, ServerMessageObserver serverMessageObserver) {
    this.serverUrl = serverUrl;
    this.httpCommunicator = new HttpCommunicator(serverUrl);
    this.currAvailableGames = new ArrayList<>();
    this.serverMessageObserver = serverMessageObserver;
    this.currState = ClientState.PRE_LOGIN;
    this.currAuthToken = null;
  }


  /**
   * Given an array of user input strings, pass it to the proper command processor.
   *
   * @param userInputArr  array of user inputs taken from the command line.
   * @return              response string (passed up from individual command processors)
   * @throws CommandException   if an unrecognized command is passed to this method or any of the individual processors
   * @throws ResponseException  if a ResponseException is thrown by the server facade upon processing
   */
  public String processUserInputArr(String[] userInputArr) throws CommandException, ResponseException {
    if (userInputArr.length == 0) {
      return "please provide a command";
    }

    // Command filter
    String desiredCmdStr = userInputArr[0].toLowerCase();
    for (var cmd : Command.Commands.values()) {
      if (desiredCmdStr.matches(cmd.getCmdRegex())) {
        return processCommand(cmd, userInputArr);
      }
    }

    // No known command detected
    throw new CommandException(String.format("invalid command \"%s\"", desiredCmdStr));
  }


  private String processCommand(Command.Commands command, String[] userInputArr) throws CommandException, ResponseException {
    if (command == Command.Commands.HELP) {
      return help();
    }

    return switch (currState) {
      case PRE_LOGIN -> processPreLoginCommand(command, userInputArr);
      case POST_LOGIN -> processPostLoginCommand(command, userInputArr);
      case GAMEPLAY -> processGameplayCommand(command, userInputArr);
    };
  }

  private String processPreLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    return switch (command) {
      case REGISTER -> register(userInputArr);
      case LOGIN -> login(userInputArr);
      case QUIT -> TERMINATE;  // Will exit the program upon being caught by the REPL
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }

  private String processPostLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    return switch (command) {
      case LOGOUT -> logout();
      case LIST -> list();
      case CREATE -> create(userInputArr);
      case JOIN -> join(userInputArr);
      case OBSERVE -> observe(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  private String processGameplayCommand(Command.Commands command, String[] userInputArr) throws CommandException {
    return switch (command) {
      case REDRAW -> redraw();
      case MOVE -> makeMove(userInputArr);
      case RESIGN -> resign();
      case LEAVE -> leave();
      case HIGHLIGHT -> highlight(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }

  private String redraw() {
    //TODO Implement
    return null;
  }

  private String resign() {
    //TODO Implement
    return null;
  }

  private String leave() {
    //TODO Implement
    currState = ClientState.POST_LOGIN;
    return null;
  }

  private String makeMove(String[] userInputArr) {
    //TODO Implement
    return null;
  }

  private String highlight(String[] userInputArr) {
    //TODO Implement
    return null;
  }

  private String help() {
    StringBuilder sb = new StringBuilder();

    for (var availableCommand : currState.getAssociatedCommands()) {
      sb.append(" ").append(availableCommand).append("\n");  //TODO should the buffer space be derived from the console printer?
    }
    return sb.toString();
  }

  private String register(String[] userInput) throws ResponseException {
    RegisterResponse registerResponse = httpCommunicator.registerUser(userInput[1], userInput[2], userInput[3]);
    return String.format("user '%s' registered%n%s", registerResponse.getUsername(), login(userInput));
  }


  private String login(String[] userInput) throws ResponseException {
    LoginResponse loginResponse = httpCommunicator.login(userInput[1], userInput[2]);
    currAuthToken = loginResponse.getAuthToken();
    currState = ClientState.POST_LOGIN;
    return String.format("user '%s' logged in%n", loginResponse.getUsername());
  }


  private String join(String[] userInput) throws ResponseException, CommandException {
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
    currState = ClientState.GAMEPLAY;

    return String.format("you've joined game #%d as %s", localGameNum, playerColor);
  }

  private String observe(String[] userInput) throws ResponseException {
    //TODO Implement observe logic
    return null;
  }


  private String create(String[] userInput) throws ResponseException {
    CreateGameResponse createGameResponse = httpCommunicator.createGame(userInput[1], currAuthToken);
    return String.format("'%s' created", userInput[1]);
  }


  private String list() throws ResponseException {
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


  private String logout() throws ResponseException {
    httpCommunicator.logout(currAuthToken);
    currAuthToken = null;
    currState = ClientState.PRE_LOGIN;
    return "logged out successfully.";
  }


  private static void verifyNumArgs(Command.Commands command, String[] userInput) throws CommandException {
    int expectedNumArgs = command.getNumArguments();
    int actualNumArgs = userInput.length - 1; // -1 to account for the command itself
    if ((expectedNumArgs) != actualNumArgs) {
      throw new CommandException(String.format("'%s' expected %d arguments (%d provided)",
              command.getName(), expectedNumArgs, actualNumArgs));
    }
  }
}
