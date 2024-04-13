import chess.*;
import consoleDraw.ConsoleDraw;

import java.util.Collection;
import java.util.Collections;

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

  public static String printChessBoard(ChessGame game, boolean isWhite, ChessPosition highlightedPosition) {
    boolean highlightValidMoves = highlightedPosition != null;
    Collection<ChessPosition> validPositions = Collections.emptyList();
    ChessBoard retrievedBoard = game.getBoard();

    if (highlightValidMoves) {
      Collection<ChessMove> validMoves = game.validMoves(highlightedPosition);
      var highlightedPiece = retrievedBoard.getPiece(highlightedPosition);

      if (highlightedPiece == null || highlightedPiece.getTeamColor() != game.getTeamTurn() || validMoves == null) {
        highlightValidMoves = false;
      }
      else {
        validPositions = validMoves.stream().map(ChessMove::getEndPosition).toList();
      }
    }

    final int startVal = isWhite ? 8 : 1;
    final int endVal = isWhite ? 0 : 9;
    final int rowIncrement = isWhite ? -1 : 1;
    final int colIncrement = -rowIncrement;

    var board = retrievedBoard.getBoardMatrix();
    StringBuilder sb = new StringBuilder();
    sb.append("\n").append(ConsoleDraw.SET_TEXT_COLOR_BLACK);


    String darkColor;
    String lightColor;

    for (int currRow = startVal; currRow != endVal; currRow += rowIncrement) {
      ChessPiece[] row = board[currRow - 1];

      for (int currCol = endVal + colIncrement; currCol != startVal + colIncrement; currCol += colIncrement) {
        ChessPosition currPosition = new ChessPosition(currRow, currCol);
        ChessPiece piece = row[currCol - 1];
        String pieceStr = (piece == null) ? ConsoleDraw.EMPTY : piece.toString();

        if (highlightValidMoves && validPositions.contains(currPosition)) {
          darkColor = DARK_HIGHLIGHT_BG_COLOR;
          lightColor = LIGHT_HIGHLIGHT_BG_COLOR;
        }
        else if (currPosition.equals(highlightedPosition)) {
          darkColor = INITIAL_BG_COLOR;
          lightColor = INITIAL_BG_COLOR;
        }
        else {
          darkColor = DARK_SQUARE_BG_COLOR;
          lightColor = LIGHT_SQUARE_BG_COLOR;
        }
        boolean isDark = ((currRow + currCol) % 2 == 1);
        sb.append(isDark ? darkColor : lightColor).append(pieceStr);
      }
      sb.append(RESET_BG_COLOR).append("\n");
    }

    sb.append(RESET_BG_COLOR);
    return sb.toString();
  }
}
