import chess.ChessGame;
import chess.ChessPiece;
import consoleDraw.ConsoleDraw;

import static consoleDraw.ConsoleDraw.WHITE_KING;
import static consoleDraw.ConsoleDraw.WHITE_QUEEN;

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

  public static void printChessBoard(ChessGame game, boolean isWhite) {
    StringBuilder sb = new StringBuilder();
    var board = game.getBoard().getBoard();
    sb.append(ConsoleDraw.SET_TEXT_COLOR_BLACK);

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
