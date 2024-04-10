import chess.ChessGame;

import static consoleDraw.consoleDraw.WHITE_KING;
import static consoleDraw.consoleDraw.WHITE_QUEEN;

public class ClientConsoleControl {

  private ClientConsoleControl() {}

  private static final String WELCOME_MESSAGE = WHITE_KING + " Chess Client " + WHITE_QUEEN;
  private static final String PROMPT_ICON = ">>>";

  public static void displayWelcomeMessage() {
    ClientConsoleControl.printInfoMessage(WELCOME_MESSAGE);
  }

  //TODO stylize unique messages

  public static void printPromptIcon() {
    System.out.printf("%n%s ", PROMPT_ICON);
  }

  public static void printNeutralMessage(String message) {
    System.out.println(message);
  }

  public static void printInfoMessage(String message) {
    System.out.println(message);
  }

  public static void printErrorMessage(String message) {
    System.out.printf("<!> %s <!>%n", message);
  }

  public static void printNotification(String message) {
    System.out.println(message);
  }

  public static void printChessBoard(ChessGame game) {
    //TODO migrate printable logic from chessboard to here
    System.out.println(game.getBoard().getPrintable(true));
  }
}
