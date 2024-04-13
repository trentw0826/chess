import chess.ChessGame;
import chess.ChessPosition;
import command.Command;
import command.CommandException;
import exception.ResponseException;
import facade.ServerMessageObserver;
import playerColor.PlayerColor;
import request.webSocketMessages.serverMessages.Error;
import request.webSocketMessages.serverMessages.LoadGame;
import request.webSocketMessages.serverMessages.Notification;
import request.webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static playerColor.PlayerColor.*;

public class ClientHandler implements ServerMessageObserver {

  public static final String TERMINATE = "terminate";

  private final Scanner scanner = new Scanner(System.in);
  private final ServerFacade serverFacade;
  private String currUsername;
  private ClientState currState;
  private PlayerColor currColor;
  private ChessGame currGame;

  public ClientHandler(String serverUrl) {
    this.serverFacade = new ServerFacade(serverUrl, this);
    this.currState = ClientState.PRE_LOGIN;
    this.currColor = null;
  }


  /**
   * Controls the read->eval->print interaction between the user's console
   * and the command processor.
   */
  public void repl() {
    ClientConsoleControl.displayWelcomeMessage();
    while (true) {
      String[] userInput = getUserInput();
      String processedOutput;

      try {
        processedOutput = processUserInputArr(userInput);
        if (processedOutput.equals(ClientHandler.TERMINATE)) {
          break;
        }
        ClientConsoleControl.printNeutralMessage(processedOutput);
      }
      // TODO Implement filtering of Command and Response exceptions into user-friendly messages
      catch (CommandException e) {
        ClientConsoleControl.printErrorMessage(e.getMessage());
      }
      catch (ResponseException e) {
        ClientConsoleControl.printErrorMessage(String.format("%s [%d]", e.getMessage(), e.getStatusCode()));
      }
    }
    System.exit(0);
  }


  @Override
  public void notifyOfMessage(ServerMessage message) {
    switch (message.getServerMessageType()) {
      case NOTIFICATION -> ClientConsoleControl.printNotification(((Notification) message).getMessage());
      case ERROR -> ClientConsoleControl.printErrorMessage(((Error) message).getErrorMessage());
      case LOAD_GAME -> {
        currGame = ((LoadGame) message).getGame();
        System.out.print(drawCurrGame());
      }
    }
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
      case ClientState.OBSERVING -> processObservingCommand(command, userInputArr);
    };
  }


  private String processPreLoginCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    verifyNumArgs(command, userInputArr);

    return switch (command) {
      case REGISTER -> {
        String registerResult = serverFacade.register(userInputArr);
        String loginResult = serverFacade.login(userInputArr);
        currUsername = userInputArr[1];
        currState = ClientState.POST_LOGIN;
        yield registerResult + loginResult;
      }
      case LOGIN -> {
        String loginResult = serverFacade.login(userInputArr);
        currUsername = userInputArr[1];
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
        currUsername = null;
        currState = ClientState.PRE_LOGIN;
        yield logoutResult;
      }
      case LIST -> serverFacade.list();
      case CREATE -> serverFacade.create(userInputArr);
      case JOIN -> {
        String joinResult = serverFacade.join(userInputArr);
        currState = ClientState.GAMEPLAY;
        currColor = PlayerColor.valueOf(userInputArr[2].toUpperCase());
        yield joinResult;
      }
      case OBSERVE -> {
        String observeResult = serverFacade.observe(userInputArr);
        currState = ClientState.OBSERVING;
        currColor = null;
        yield observeResult;
      }
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  private String processGameplayCommand(Command.Commands command, String[] userInputArr) throws ResponseException, CommandException {
    return switch (command) {
      case DRAW -> drawCurrGame();
      case MOVE -> serverFacade.makeMove(userInputArr);
      case RESIGN -> serverFacade.resign();
      case LEAVE -> {
        String leaveResult = serverFacade.leave();
        currState = ClientState.POST_LOGIN;
        currColor = null;
        currGame = null;
        yield leaveResult;
      }
      case HIGHLIGHT -> highlight(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  private String processObservingCommand(Command.Commands command, String[] userInputArr) throws CommandException, ResponseException {
    return switch (command) {
      case DRAW -> drawCurrGame();
      case LEAVE -> {
        String leaveResult = serverFacade.leave();
        currState = ClientState.POST_LOGIN;
        currColor = null;
        yield leaveResult;
      }
      case HIGHLIGHT -> highlight(userInputArr);
      default -> throw new CommandException(String.format("command '%s' not currently available", command.getName()));
    };
  }


  /**
   * Return current game's board as a printable string
   */
  private String drawCurrGame() {
    return ClientConsoleControl.printChessBoard(currGame, currColor == WHITE, null);
  }


  /**
   * @param userInputArr user input array containing desired chess position at index 0
   * @return  printable board string with valid moves highlighted
   */
  public String highlight(String[] userInputArr) {
    ChessPosition highlightedPosition = new ChessPosition(userInputArr[0]);
    return ClientConsoleControl.printChessBoard(currGame, currColor == WHITE, highlightedPosition);
  }


  /**
   * Return current available commands as a printable string
   */
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


  /**
   * @return  Line of user input from the console
   */
  private String[] getUserInput() {
    ClientConsoleControl.printPromptIcon(currUsername);
    return scanner.nextLine().trim().split("\\s+");
  }
}
