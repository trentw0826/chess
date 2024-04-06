package ui;

import exception.ResponseException;
import response.*;
import model.*;

import java.util.ArrayList;

import static java.lang.System.exit;
import static ui.Command.Commands.*;

public class CommandProcessor {
  private static final String SERVER_URL = "http://localhost:8080";

  private static final ArrayList<Command.Commands> PRE_LOGIN_COMMANDS = new ArrayList<>();
  private static final ArrayList<Command.Commands> POST_LOGIN_COMMANDS = new ArrayList<>();

  static {
    PRE_LOGIN_COMMANDS.add(REGISTER);
    PRE_LOGIN_COMMANDS.add(LOGIN);
    PRE_LOGIN_COMMANDS.add(HELP);
    PRE_LOGIN_COMMANDS.add(QUIT);

    POST_LOGIN_COMMANDS.add(LIST);
    POST_LOGIN_COMMANDS.add(CREATE);
    POST_LOGIN_COMMANDS.add(JOIN);
    POST_LOGIN_COMMANDS.add(OBSERVE);
    POST_LOGIN_COMMANDS.add(HELP);
    POST_LOGIN_COMMANDS.add(LOGOUT);
  }

  private final ServerFacade serverFacade;
  private final ArrayList<Command.Commands> currAvailableCommands;
  private final ArrayList<GameData> currAvailableGames;
  private boolean isLoggedIn;
  private String currAuthToken;

  public CommandProcessor() {
    this.serverFacade = new ServerFacade(SERVER_URL);
    this.currAvailableCommands = PRE_LOGIN_COMMANDS;
    this.currAvailableGames = new ArrayList<>();
    this.isLoggedIn = false;
    this.currAuthToken = null;
  }


  public void processUserInputArr(String[] userInputArr) throws CommandException, ResponseException {
    if (userInputArr.length == 0) {
      System.out.println(" please provide a command");
      return;
    }

    Command.Commands command;
    // Filters out commands that don't exist at all
    try {
      command = Command.Commands.valueOf(userInputArr[0].toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new CommandException(String.format("invalid command \"%s\"", userInputArr[0]));
    }

    // Filters out commands based on login level
    processCommand(userInputArr, command);
  }


  private void processCommand(String[] userInputArr, Command.Commands command) throws CommandException, ResponseException {
    if (command == HELP) {
      for (var availableCommand : currAvailableCommands) {
        System.out.println(" " + availableCommand);
      }
    }
    else {
      if (isLoggedIn) {
        processPostLoginCommand(command, userInputArr);
      }
      else {
        processPreLoginCommand(command, userInputArr);
      }
    }
  }


  private void processPreLoginCommand(Command.Commands command, String[] userInput) throws ResponseException, CommandException {
    verifyNumArgs(command, userInput);

    switch (command) {
      case REGISTER:
        RegisterResponse registerResponse = serverFacade.registerUser(userInput[1], userInput[2], userInput[3]);
        System.out.printf(" user '%s' registered%n", registerResponse.getUsername());
        login(userInput);
        break;

      case LOGIN:
        login(userInput);
        break;

      case QUIT:
        exit(0);
        break;

      default:
        throw new CommandException("command not currently available");
    }
  }


  private void login(String[] userInput) throws ResponseException {
    LoginResponse loginResponse = serverFacade.login(userInput[1], userInput[2]);
    currAuthToken = loginResponse.getAuthToken();
    isLoggedIn = true;
    System.out.printf(" user '%s' logged in%n", loginResponse.getUsername());
    currAvailableCommands.clear();
    currAvailableCommands.addAll(POST_LOGIN_COMMANDS);
  }


  private void processPostLoginCommand(Command.Commands command, String[] userInput) throws ResponseException, CommandException {
    verifyNumArgs(command, userInput);

    switch (command) {
      case LOGOUT:
        logout();
        break;

      case LIST:
        list();
        break;

      case CREATE:
        create(userInput);
        break;

      case JOIN:
        join(userInput);
        break;

      case OBSERVE:
        observe(userInput);
        break;

      default:
        throw new CommandException("command not currently available");
    }
  }

  private void join(String[] userInput) throws ResponseException {
    int localGameNum = Integer.parseInt(userInput[1]);
    int gameId = currAvailableGames.get(localGameNum - 1).getGameID();
    JoinGameResponse joinGameResponse = serverFacade.joinGame(currAuthToken, userInput[2], gameId);
    GameData fillerGame = new GameData("fillerGame");
    System.out.println(fillerGame.getGame().getBoard().getPrintable(true));
  }

  private void observe(String[] userInput) throws ResponseException {
    int localGameNum = Integer.parseInt(userInput[1]);
    int gameId = currAvailableGames.get(localGameNum - 1).getGameID();
    JoinGameResponse joinGameResponse = serverFacade.joinGame(currAuthToken, userInput[2], gameId);
    GameData fillerGame = new GameData("fillerGame");
    System.out.println(fillerGame.getGame().getBoard().getPrintable(true));
  }

  private void create(String[] userInput) throws ResponseException {
    CreateGameResponse createGameResponse = serverFacade.createGame(userInput[1], currAuthToken);
    System.out.printf(" game '%s' created (id:%d) ", userInput[1], createGameResponse.getGameID());
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
        System.out.printf("%d: %s", i, gameData.headerStr());
      }
    }
  }

  private void logout() throws ResponseException {
    serverFacade.logout(currAuthToken);
    isLoggedIn = false;
    currAuthToken = null;
    System.out.println(" logged out successfully.");
    currAvailableCommands.clear();
    currAvailableCommands.addAll(PRE_LOGIN_COMMANDS);
  }


  private static void verifyNumArgs(Command.Commands command, String[] userInput) throws CommandException {
    int expectedNumArgs = command.getNumArguments();
    int actualNumArgs = userInput.length - 1; // -1 to account for the command itself
    if ((expectedNumArgs) != actualNumArgs) {
      throw new CommandException(String.format("'%s' expected %d arguments (%d provided)",
              command.getCmdString(), expectedNumArgs, actualNumArgs));
    }
  }
}
