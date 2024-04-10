import command.CommandException;
import command.CommandProcessor;
import exception.ResponseException;
import facade.ServerMessageObserver;
import request.webSocketMessages.serverMessages.*;
import request.webSocketMessages.serverMessages.Error;
import request.webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static command.CommandProcessor.TERMINATE;

/**
 * Center for handling user interactions.
 * Acts as an in-between for the user's console and the server facade
 */
public class Repl implements ServerMessageObserver {

  private final Scanner scanner = new Scanner(System.in);
  private final CommandProcessor processor;

  public Repl(String serverUrl) {
    this.processor = new CommandProcessor(serverUrl, this);
  }

  /**
   * Controls the read->eval->print interaction between the user's console
   * and the command processor.
   */
  public void repl() {
    ClientConsoleControl.displayWelcomeMessage();
    while (true) {
      String[] userInput = getUserInput();
      String processedOutput = "";

      try {
        processedOutput = processor.processUserInputArr(userInput);
        if (processedOutput.equals(TERMINATE)) {
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
      case LOAD_GAME -> ClientConsoleControl.printChessBoard(((LoadGame) message).getGame());
    }
  }

  /**
   * @return  Line of user input from the console
   */
  private String[] getUserInput() {
    ClientConsoleControl.printPromptIcon();
    return scanner.nextLine().trim().split("\\s+");
  }
}
