package command;

public class CommandException extends Exception {

  public CommandException(String message) {
    super(message + " ('help' for available commands)");
  }
}
