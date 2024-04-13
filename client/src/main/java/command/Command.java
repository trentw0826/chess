package command;

import consoleDraw.ConsoleDraw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Command {

  private static final String USERNAME_ARG = "<USERNAME>";
  private static final String PASSWORD_ARG = "<PASSWORD>";
  private static final String EMAIL_ARG = "<EMAIL>";
  private static final String GAME_NAME_ARG = "<GAME_NAME>";
  private static final String GAME_ID_ARG = "<GAME_ID>";
  private static final String COLOR_ARG = "<white|black>";

  /**
   * Enum class to contain command types. Includes member variables to hold each command as a string
   * as well as its description
   */
  public enum Commands {
    // general command(s)
    HELP("help", "display available commands"),
    // pre-login
    QUIT("quit", "exit the program"),
    LOGIN("login", "log in to your account", USERNAME_ARG, PASSWORD_ARG),
    REGISTER("register", "create a new account", USERNAME_ARG, PASSWORD_ARG, EMAIL_ARG),
    // post-login
    LOGOUT("logout", "log out from your account"),
    CREATE("create", "create a new chess game", GAME_NAME_ARG),
    LIST("list", "list all available chess games"),
    JOIN("join", "join an existing chess game as the given color", GAME_ID_ARG, COLOR_ARG),
    OBSERVE("observe", "observe an existing chess game", GAME_ID_ARG),
    // gameplay
    DRAW("draw", "re-load the board of your current game to your screen"),
    MOVE("[a-h][1-8][a-h][1-8](=[QRNB])?", "make a chess move (eg. e4e5)"),
    RESIGN("resign", "admit defeat"),
    LEAVE("leave", "leave the game (doesn't resign)"),
    HIGHLIGHT("[a-hA-H][1-8]", "highlight the valid moves for a given piece (eg. e4)");

    private final String cmdRegex;
    private final String cmdDescription;
    private final Collection<String> arguments;


    Commands(String cmdRegex, String cmdDescription, String... arguments) {
      this.cmdRegex = regifyStr(cmdRegex);
      this.cmdDescription = cmdDescription;
      this.arguments = new ArrayList<>();
      Collections.addAll(this.arguments, arguments);
    }

    public String getCmdRegex() {
      return cmdRegex;
    }

    public String getName() {
      return name().toLowerCase();
    }

    @Override
    public String toString() {
      var sb = new StringBuilder();

      sb.append(ConsoleDraw.boldString(name().toLowerCase()));
      for (var arg : arguments) {
        sb.append(" ").append(arg);
      }

      sb.append(" : ").append(ConsoleDraw.italicizeString(cmdDescription));
      return sb.toString();
    }

    public int getNumArguments() {
      return arguments.size();
    }
  }

  private static String regifyStr(String str) {
    return "^" + str + "$";
  }
}
