package ui;

import exception.ResponseException;
import response.*;

import java.util.*;

import static ui.Command.Commands.*;
import static java.lang.System.exit;

/**
 * Class interacts with the client, both processing input and displaying output on the console
 */
public class ClientUI {

  private static final String SERVER_URL = "http://localhost:8080";
  private static final ServerFacade serverFacade = new ServerFacade(SERVER_URL);

  private static final Scanner SCANNER = new Scanner(System.in);

  private static final String WELCOME_MESSAGE = TextStyle.WHITE_KING + " Chess Client " + TextStyle.WHITE_QUEEN;

  private boolean isUserActive;
  private Collection<Command.Commands> currAvailableCommands;

  // Set of before-login commands
  private static final Collection<Command.Commands> PRE_LOGIN_COMMANDS = Set.of(
          REGISTER,
          LOGIN,
          HELP,
          QUIT
          );

  // Set of after-login commands
  private static final Collection<Command.Commands> POST_LOGIN_COMMANDS = Set.of(
          LIST_GAMES,
          CREATE_GAME,
          JOIN_GAME,
          JOIN_OBSERVER,
          LOGOUT,
          HELP
  );

  private String currUsername;
  private String currAuthToken;

  public ClientUI() {}

  /**
   * Maintains the program/user interaction loop (read input, validate it,
   * convert to command, execute command, repeat), which continues until client logs out
   */
  public void repl() {
    currAvailableCommands = PRE_LOGIN_COMMANDS;
    isUserActive = true;

    // program/user interaction loop
    displayWelcomeMessage();
    while (isUserActive) {
      String[] userInputArr = getValidUserInput();
      processCommand(userInputArr);
    }

    exitProgram();
  }


  /**
   * Given an available command in the form of a string, returns the associated command.
   *
   * @param commandString command in string form
   * @return              command associated with 'commandString'
   */
  private Command.Commands retrieveCommand(String commandString) {
    for (Command.Commands command : currAvailableCommands) {
      if (command.getCmdString().equalsIgnoreCase(commandString)) {
        return command;
      }
    }
    // String doesn't match an available command
    throw new IllegalArgumentException("Invalid command: " + commandString);
  }


  /**
   * Get a valid user input from the command line.
   * A valid input is defined as a string that, when trimmed and lower-cased,
   * matches one of the available commands in the 'availableCommands' set
   *
   * @return  validated command line input
   */
  private String[] getValidUserInput() {
    String[] userInputArr;
    boolean inputValidated = false;

    displayUserInputPrompt();
    userInputArr = getUserInput();
    inputValidated = validateUserInput(userInputArr);

    while(!inputValidated) {
      displayAssistedUserInputPrompt();
      userInputArr = getUserInput();
      inputValidated = validateUserInput(userInputArr);
    }

    return userInputArr;
  }


  /**
   * Return the trimmed and lower-cased user input string.
   *
   * @return trimmed and lower-cased user input
   */
  private static String[] getUserInput() {
    return SCANNER.nextLine().trim().toLowerCase().split(" ");
  }


  /**
   * Given a string entered on the command line, return if that string
   * corresponds to a currently available command.
   *
   * @param userInputArr command line input array
   * @return          if the desired command corresponds to some 'commandStr' in the 'Commands' enum class
   */
  private boolean validateUserInput(String[] userInputArr) {
    if (userInputArr == null || userInputArr.length == 0) {
      return false;
    }

    String desiredCommand = userInputArr[0];

    for (var command : currAvailableCommands) {
      if (command.getCmdString().equalsIgnoreCase(desiredCommand)) {
        // Total # of parameters (total # of args + 1 for command itself)
        return (command.getNumArguments() + 1) == userInputArr.length;
      }
    }
    return false;
  }


  // welcome message
  private void displayWelcomeMessage() {
    System.out.println(TextStyle.underlineString(WELCOME_MESSAGE));
  }

  // characters to prompt user input
  private void displayUserInputPrompt() {
    String inputPrompt = (currUsername == null) ? ">>> " : ("[" + currUsername + "] >>> ");
    System.out.print(inputPrompt);
  }

  // input prompt with extra assistance for understanding commands
  private void displayAssistedUserInputPrompt() {
    System.out.printf(
            " invalid command (type '%s' for available commands)%n",
            TextStyle.boldString(HELP.getCmdString()));
    displayUserInputPrompt();
  }


  /**
   * Given a valid command, call associated program methods (as defined in phase 5 specs).
   *
   * @param userInputArr valid array of user input arguments
   */
  private void processCommand(String[] userInputArr) {
    Command.Commands command = retrieveCommand(userInputArr[0]);

    try {
      switch (command) {
        case REGISTER:
          register(userInputArr);
          login(userInputArr);
          break;

        case LOGIN:
          login(userInputArr);
          break;

        case LIST_GAMES:
          listGames();
          break;

        case CREATE_GAME:
          createGame(userInputArr);
          break;

        case JOIN_GAME:
          joinGame();
          break;

          //TODO is the observer case even necessary?
        case JOIN_OBSERVER:
          System.out.println("Observing a game...");
          break;

        case LOGOUT:
          logout();
          break;

        case HELP:
          displayAvailableCommands();
          break;

        case QUIT:
          isUserActive = false;
          break;

        default:
          throw new IllegalArgumentException("Illegal command passed");
      }
    }
    catch (ResponseException e) {
      System.out.println(e.getMessage());
    }
  }


  private void register(String[] userInputArr) throws ResponseException {
    System.out.println("Registering...");
    RegisterResponse registerResponse = serverFacade.registerUser(userInputArr[1], userInputArr[2], userInputArr[3]);
    System.out.println("user \"" + registerResponse.getUsername() + "\" registered!");
  }


  private void login(String[] userInputArr) throws ResponseException {
    System.out.println("Logging in...");
    LoginResponse loginResponse = serverFacade.login(userInputArr[1], userInputArr[2]);

    currUsername = loginResponse.getUsername();
    currAuthToken = loginResponse.getAuthToken();

    currAvailableCommands = POST_LOGIN_COMMANDS;
    System.out.println("Logged in!");
  }


  private void listGames() throws ResponseException {
    System.out.println("Listing available games...");
    ListGamesResponse listGamesResponse = serverFacade.listGames(currAuthToken);
    for (var game : listGamesResponse.getGames()) {
      System.out.println(" " + game.headerStr());
    }
  }


  private void createGame(String[] userInputArr) throws ResponseException {
    System.out.println("Creating a new game...");
    CreateGameResponse createGameResponse = serverFacade.createGame(userInputArr[1], currAuthToken);
    System.out.println("Success [" + createGameResponse + "]");
  }


  private void logout() throws ResponseException {
    System.out.println("Logging out...");
    currAvailableCommands = PRE_LOGIN_COMMANDS;
    serverFacade.logout(currAuthToken);
    currUsername = null;
    currAuthToken = null;
  }

  
  private void joinGame() {
    System.out.println("Joining a game...");
    // TODO Implement join game functionality
  }


  private void displayAvailableCommands() {
    for (var command : currAvailableCommands) {
      System.out.println(" " + command);
    }
  }

  
  private static void exitProgram() {
    System.out.println("Exiting program...");
    exit(0);
  }
}
