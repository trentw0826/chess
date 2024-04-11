import command.CommandException;
import exception.ResponseException;

import java.util.Scanner;


/**
 * Center for handling user interactions.
 * Acts as an in-between for the user's console and the server facade
 */
public class Repl {

  private final Scanner scanner = new Scanner(System.in);
  private final ClientHandler clientHandler;

  public Repl(String serverUrl) {
    this.clientHandler = new ClientHandler(serverUrl);
  }

  /**
   * Controls the read->eval->print interaction between the user's console
   * and the command processor.
   */
  //TODO Combine with client handler
  public void repl() {
    ClientConsoleControl.displayWelcomeMessage();
    while (true) {
      String[] userInput = getUserInput();
      String processedOutput;

      try {
        processedOutput = clientHandler.processUserInputArr(userInput);
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


  /**
   * @return  Line of user input from the console
   */
  private String[] getUserInput() {
    ClientConsoleControl.printPromptIcon();
    return scanner.nextLine().trim().split("\\s+");
  }
}
