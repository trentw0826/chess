import chess.ChessGame;
import chess.ChessPiece;
import consoleDraw.ConsoleDraw;

import static consoleDraw.ConsoleDraw.*;

public class ClientConsoleControl {

  private ClientConsoleControl() {}

  private static final String SET_TEXT_COLOR_DEFAULT = SET_TEXT_COLOR_LIGHT_GREY;
  private static final String SET_TEXT_COLOR_NEUTRAL = SET_TEXT_COLOR_WHITE;

  private static final String TITLE = "Chess Client";
  private static final String WELCOME_MESSAGE = String.format("%s%s%s %s %s%s%n", SET_TEXT_COLOR_DEFAULT,
          WHITE_PAWN, WHITE_KING, TITLE, WHITE_QUEEN, WHITE_PAWN);
  private static final String PROMPT_ICON = ">>>";

  public static void displayWelcomeMessage() {
    ClientConsoleControl.printNeutralMessage(WELCOME_MESSAGE);
  }

  public static void printPromptIcon(String username) {
    String userTag = username == null ? "logged out" : username;
    System.out.printf("%n%s[%s] %s%s ", SET_TEXT_COLOR_DEFAULT, userTag, PROMPT_ICON, SET_TEXT_COLOR_DEFAULT);
  }

  public static void printSoftMessage(String message) {
    System.out.printf("%n%s%s%s ", SET_TEXT_COLOR_DEFAULT, message, SET_TEXT_COLOR_DEFAULT);
  }

  public static void printNeutralMessage(String message) {
    System.out.printf(message);
  }

  public static void printInfoMessage(String message) {
    System.out.printf("%s[~]%s %s %s[~]%s%n", SET_TEXT_COLOR_GREEN, SET_TEXT_COLOR_DEFAULT, message,
            SET_TEXT_COLOR_GREEN, SET_TEXT_COLOR_DEFAULT);
  }

  public static void printErrorMessage(String message) {
    System.out.printf("%s<!>%s %s %s<!>%s%n", SET_TEXT_COLOR_RED, SET_TEXT_COLOR_DEFAULT, message,
            SET_TEXT_COLOR_RED, SET_TEXT_COLOR_DEFAULT);
  }

  public static void printNotification(String message) {
    System.out.printf("%s[-]%s %s %s[-]%s%n", SET_TEXT_COLOR_YELLOW, SET_TEXT_COLOR_DEFAULT, message,
            SET_TEXT_COLOR_YELLOW, SET_TEXT_COLOR_DEFAULT);
  }

  public static void printChessBoard(ChessGame game, boolean isWhite) {
    var board = game.getBoard().getBoard();
    StringBuilder sb = new StringBuilder();
    sb.append("\n").append(ConsoleDraw.SET_TEXT_COLOR_BLACK);

    int startRow = isWhite ? 8 : 1;
    int endRow = isWhite ? 0 : 9;
    int rowIncrement = isWhite ? -1 : 1;

    for (int i = startRow; i != endRow; i += rowIncrement) {
      ChessPiece[] row = board[i - 1];
      final int startCol = isWhite ? 0 : 7;
      final int endCol = isWhite ? 8 : -1;
      final int colIncrement = isWhite ? 1 : -1;

      for (int j = startCol; j != endCol; j += colIncrement) {
        ChessPiece piece = row[j];
        String pieceStr = (piece == null) ? ConsoleDraw.EMPTY : piece.toString();
        boolean isDark = (i + j) % 2 == 1;
        sb.append(isDark ? ConsoleDraw.DARK_SQUARE_BG_COLOR : ConsoleDraw.LIGHT_SQUARE_BG_COLOR).append(pieceStr);
      }
      sb.append(ConsoleDraw.RESET_BG_COLOR).append("\n");
    }
    sb.append(ConsoleDraw.SET_TEXT_COLOR_WHITE);

    System.out.println(sb);
  }
}
