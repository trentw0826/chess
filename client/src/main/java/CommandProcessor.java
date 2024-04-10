import command.Command;
import command.CommandException;
import exception.ResponseException;
import facade.ServerMessageObserver;

public class CommandProcessor {

  public static final String TERMINATE = "terminate";

  private final ServerFacade serverFacade;
  private ClientState currState;

  public CommandProcessor(String serverUrl, ServerMessageObserver serverMessageObserver) {
    this.serverFacade = new ServerFacade(serverUrl, serverMessageObserver);
    this.currState = ClientState.PRE_LOGIN;
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
      case ClientState.PRE_LOGIN -> processPreLoginCommand(command, userInputArr);
      case ClientState.POST_LOGIN -> processPostLoginCommand(command, userInputArr);
      case ClientState.GAMEPLAY -> processGameplayCommand(command, userInputArr);
    };
  }


  private String processPreLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    return switch (command) {
      case REGISTER -> {
        String registerResult = serverFacade.register(userInputArr);
        String loginResult = serverFacade.login(userInputArr);
        currState = ClientState.POST_LOGIN;
        yield registerResult + loginResult;
      }
      case LOGIN -> {
        String loginResult = serverFacade.login(userInputArr);
        currState = ClientState.POST_LOGIN;
        yield loginResult;
      }
      case QUIT -> TERMINATE;  // Will exit the program upon being caught by the REPL
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  private String processPostLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    return switch (command) {
      case LOGOUT -> {
        String logoutResult = serverFacade.logout();
        currState = ClientState.POST_LOGIN;
        yield logoutResult;
      }
      case LIST -> serverFacade.list();
      case CREATE -> serverFacade.create(userInputArr);
      case JOIN -> {
        String joinResult = serverFacade.join(userInputArr);
        currState = ClientState.GAMEPLAY;
        yield joinResult;
      }
      case OBSERVE -> serverFacade.observe(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  private String processGameplayCommand(Command.Commands command, String[] userInputArr) throws CommandException {
    return switch (command) {
      case REDRAW -> serverFacade.redraw();
      case MOVE -> serverFacade.makeMove(userInputArr);
      case RESIGN -> serverFacade.resign();
      case LEAVE -> {
        String leaveResult = serverFacade.leave();
        currState = ClientState.POST_LOGIN;
        yield leaveResult;
      }
      case HIGHLIGHT -> serverFacade.highlight(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  private String help() {
    StringBuilder sb = new StringBuilder();

    for (var availableCommand : currState.getAssociatedCommands()) {
      //TODO should the buffer space be derived from the console printer?
      sb.append(" ").append(availableCommand).append("\n");
    }
    return sb.toString();
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
