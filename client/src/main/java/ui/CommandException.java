package ui;

public class CommandException extends Exception {

  public CommandException(String message) {
    super(message + " ('help' for available command usage)");
  }
}
