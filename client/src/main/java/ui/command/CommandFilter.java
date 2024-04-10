package ui.command;

import exception.ResponseException;
import playerColor.PlayerColor;
import response.*;
import model.*;
import ui.facade.ServerFacade;
import ui.facade.WebsocketFacade;

import java.util.ArrayList;

import static java.lang.System.exit;

public class CommandFilter {
  private static final String SERVER_URL = "http://localhost:8080";

  private final ServerFacade serverFacade;
  private ClientState currClientState;
  private final ArrayList<GameData> currAvailableGames;
  private String currAuthToken;

  //TODO Improve breaking up of responsibilities by migrating any printing over to the UserInterface class
  public CommandFilter() {
    this.serverFacade = new ServerFacade(SERVER_URL);
    this.currClientState = ClientState.PRE_LOGIN;
    this.currAvailableGames = new ArrayList<>();
    this.currAuthToken = null;
  }


  public void processUserInputArr(String[] userInputArr) throws CommandException, ResponseException {
    if (userInputArr.length == 0) {
      System.out.println(" please provide a command");
      return;
    }

    // Command filter
    String desiredCmdStr = userInputArr[0].toLowerCase();
    for (var cmd : Command.Commands.values()) {
      if (desiredCmdStr.matches(cmd.getCmdRegex())) {
        processCommand(userInputArr, cmd);
        return;
      }
    }

    // No known command detected
    throw new CommandException(String.format("invalid command \"%s\"", desiredCmdStr));
  }


  private void processCommand(String[] userInputArr, Command.Commands command) throws CommandException, ResponseException {
    if (command == Command.Commands.HELP) {
      help();
      return;
    }

    switch (currClientState) {
      case PRE_LOGIN -> processPreLoginCommand(command, userInputArr);
      case POST_LOGIN -> processPostLoginCommand(command, userInputArr);
      case GAMEPLAY -> processGameplayCommand(command, userInputArr);
    }
  }

  private void processPreLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    switch (command) {
      case REGISTER -> register(userInputArr);
      case LOGIN -> login(userInputArr);
      case QUIT -> exit(0);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    }
  }

  private void processPostLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    switch (command) {
      case LOGOUT -> logout();
      case LIST -> list();
      case CREATE -> create(userInputArr);
      case JOIN -> join(userInputArr);
      case OBSERVE -> observe(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    }
  }


  private void processGameplayCommand(Command.Commands command, String[] userInputArr) throws CommandException {
    switch (command) {
      case REDRAW -> redraw();
      case MOVE -> makeMove(userInputArr);
      case RESIGN -> resign();
      case LEAVE -> leave();
      case HIGHLIGHT -> highlight(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    }
  }

  private void redraw() {
    //TODO Implement
  }

  private void resign() {
    //TODO Implement
  }

  private void leave() {
    //TODO Implement
    currClientState = ClientState.POST_LOGIN;
  }

  private void makeMove(String[] userInputArr) {
    //TODO Implement
  }

  private void highlight(String[] userInputArr) {
    //TODO Implement
  }

  private void help() {
    for (var availableCommand : currClientState.getAssociatedCommands()) {
      System.out.println(" " + availableCommand);
    }
  }

  private void register(String[] userInput) throws ResponseException {
    RegisterResponse registerResponse = serverFacade.registerUser(userInput[1], userInput[2], userInput[3]);
    System.out.printf(" user '%s' registered%n", registerResponse.getUsername());
    login(userInput);
  }


  private void login(String[] userInput) throws ResponseException {
    LoginResponse loginResponse = serverFacade.login(userInput[1], userInput[2]);
    currAuthToken = loginResponse.getAuthToken();
    System.out.printf(" user '%s' logged in%n", loginResponse.getUsername());
    currClientState = ClientState.POST_LOGIN;
  }


  private void join(String[] userInput) throws ResponseException, CommandException {
    int localGameNum = Integer.parseInt(userInput[1]);
    int gameId = 0;
    if (localGameNum > 0 && localGameNum <= currAvailableGames.size()) {
      gameId = currAvailableGames.get(localGameNum - 1).getGameID();
    }
    else {
      throw new CommandException(String.format("Game with id '%d' not locally listed ('list' for games)", localGameNum));
    }

    PlayerColor playerColor = null;
    try {
      playerColor = PlayerColor.valueOf(userInput[2].toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new CommandException("Make sure your color is 'white' or 'black'");
    }

    serverFacade.joinGame(gameId, currAuthToken, playerColor);

    WebsocketFacade websocketFacade = new WebsocketFacade(SERVER_URL);
    websocketFacade.joinPlayer(gameId, currAuthToken, playerColor);
    currClientState = ClientState.GAMEPLAY;
  }

  private void observe(String[] userInput) throws ResponseException {
    //TODO Implement observe logic
  }


  private void create(String[] userInput) throws ResponseException {
    CreateGameResponse createGameResponse = serverFacade.createGame(userInput[1], currAuthToken);
    System.out.printf(" game '%s' created", userInput[1]);
  }


  private void list() throws ResponseException {
    ListGamesResponse listGamesResponse = serverFacade.listGames(currAuthToken);
    currAvailableGames.clear();
    currAvailableGames.addAll(listGamesResponse.getGames());
    if (currAvailableGames.isEmpty()) {
      System.out.println(" no available games :(");
    }
    else {
      int i = 0;
      for (GameData gameData : currAvailableGames) {
        i++;
        System.out.printf("%d: %s%n", i, gameData.headerStr());
      }
    }
  }


  private void logout() throws ResponseException {
    serverFacade.logout(currAuthToken);
    currAuthToken = null;
    System.out.println(" logged out successfully.");
    currClientState = ClientState.PRE_LOGIN;
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
