package ui;

import exception.ResponseException;

import java.util.Scanner;

import static consoleDraw.consoleDraw.*;

public class UserInterface {
  private final CommandProcessor commandProcessor;
  private boolean isUserActive;

  private static final String WELCOME_MESSAGE = WHITE_KING + " Chess Client " + WHITE_QUEEN;

  public UserInterface() {
    this.commandProcessor = new CommandProcessor();
    this.isUserActive = true;
  }

  public void start() {
    displayWelcomeMessage();
    while (isUserActive) {
      String[] userInput = getUserInput();
      try {
        commandProcessor.processUserInputArr(userInput);
      }
      catch (CommandException e) {
        System.out.printf(" <!> %s <!>%n", e.getMessage());
      }
      catch (ResponseException e) {
        // TODO Implement filtering of ResponseExceptions into user-friendly messages
        System.out.printf(" <!> %s [%d] <!>%n", e.getMessage(), e.getStatusCode());
      }
    }
  }


  public void displayWelcomeMessage() {
    System.out.println(WELCOME_MESSAGE);
  }


  private String[] getUserInput() {
    Scanner scanner = new Scanner(System.in);
    clearScreen();
    System.out.print("\n>>> ");
    return scanner.nextLine().trim().split("\\s+");
  }
}
