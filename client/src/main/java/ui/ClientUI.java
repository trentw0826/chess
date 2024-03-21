package ui;

import exception.ResponseException;

import java.util.*;

import static ui.Command.Commands.*;
import static java.lang.System.exit;

/**
 * Class interacts with the client, both processing input and displaying output on the console
 */
public class ClientUI {

  private static final String SERVER_URL = "http://localhost:8080";
  private static final ServerFacade serverFacade = new ServerFacade(SERVER_URL);

  private static final String WELCOME_MESSAGE = TextStyle.WHITE_KING + " Chess Client " + TextStyle.WHITE_QUEEN;

  private static boolean isUserActive;
  private static final Scanner SCANNER = new Scanner(System.in);
  private static Collection<Command.Commands> currAvailableCommands;

  // Set of before-login commands
  private static final Collection<Command.Commands> PRE_LOGIN_COMMANDS = Set.of(
          HELP,
          QUIT,
          LOGIN,
          REGISTER
  );

  // Set of after-login commands
  private static final Collection<Command.Commands> POST_LOGIN_COMMANDS = Set.of(
          HELP,
          LOGOUT,
          CREATE_GAME,
          LIST_GAMES,
          JOIN_GAME,
          JOIN_OBSERVER
  );

  private ClientUI() {}

  /**
   * Maintains the program/user interaction loop (read input, validate it,
   * convert to command, execute command, repeat), which continues until client logs out
   */
  public static void repl() {
    currAvailableCommands = PRE_LOGIN_COMMANDS;
    isUserActive = true;

    // program/user interaction loop
    displayWelcomeMessage();
    while (isUserActive) {
      String[] userInputArr = getValidUserInput();
      var userCommand = retrieveCommand(userInputArr[0]);
      processCommand(userCommand);
    }

    exitProgram();
  }


  /**
   * Given an available command in the form of a string, returns the associated command.
   *
   * @param commandString command in string form
   * @return              command associated with 'commandString'
   */
  private static Command.Commands retrieveCommand(String commandString) {
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
  private static String[] getValidUserInput() {
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
  private static boolean validateUserInput(String[] userInputArr) {
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
  private static void displayWelcomeMessage() {
    System.out.println(TextStyle.underlineString(WELCOME_MESSAGE));
  }

  // characters to prompt user input
  private static void displayUserInputPrompt() {
    System.out.print(">>> ");
  }

  // input prompt with extra assistance for understanding commands
  private static void displayAssistedUserInputPrompt() {
    System.out.printf(
            " invalid command (type '%s' for available commands)%n",
            TextStyle.boldString(HELP.getCmdString()));
    displayUserInputPrompt();
  }


  /**
   * Given a valid command, call associated program methods (as defined in phase 5 specs).
   *
   * @param command a valid user input
   */
  private static void processCommand(Command.Commands command) {
    try {
      switch (command) {
      case HELP:
        displayAvailableCommands();
        break;

      case QUIT:
        isUserActive = false;
        break;

      case LOGIN:
        System.out.println("Logging in...");
        // TODO Implement login functionality
        currAvailableCommands = POST_LOGIN_COMMANDS;
        break;

      case REGISTER:
        System.out.println("Registering...");
        serverFacade.registerUser(null);
        break;

      case LOGOUT:
        System.out.println("Logging out...");
        currAvailableCommands = PRE_LOGIN_COMMANDS;
        // TODO Implement logout functionality
        break;

      case CREATE_GAME:
        System.out.println("Creating a new game...");
        // TODO Implement create game functionality
        break;

      case LIST_GAMES:
        System.out.println("Listing available games...");
        // TODO Implement list games functionality
        break;

      case JOIN_GAME:
        System.out.println("Joining a game...");
        // TODO Implement join game functionality
        break;

      case JOIN_OBSERVER:
        System.out.println("Observing a game...");
        // TODO Implement join observer functionality
        break;

      default:
        throw new IllegalArgumentException("Illegal command passed");
      }
    }
    catch (ResponseException e) {
      System.out.println(e.getMessage());
    }
  }


  /**
   * Responds to 'help command', displays all commands currently held in
   * the 'availableCommands' collection.
   */
  private static void displayAvailableCommands() {
    for (var command : currAvailableCommands) {
      System.out.println(" " + command);
    }
  }


  /**
   * Terminate the program with success
   */
  private static void exitProgram() {
    System.out.println("Exiting program...");
    exit(0);
  }
}
