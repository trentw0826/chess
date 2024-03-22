package ui;

import consoleDraw.consoleDraw;

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
  enum Commands {
    HELP("help", "display available commands"),
    QUIT("quit", "exit the program"),
    LOGIN("login", "log in to your account", USERNAME_ARG, PASSWORD_ARG),
    REGISTER("register", "create a new account", USERNAME_ARG, PASSWORD_ARG, EMAIL_ARG),
    LOGOUT("logout", "log out from your account"),
    CREATE_GAME("create", "create a new chess game", GAME_NAME_ARG),
    LIST_GAMES("list", "list all available chess games"),
    JOIN_GAME("join", "join an existing chess game as the given color", GAME_ID_ARG, COLOR_ARG),
    JOIN_OBSERVER("observe", "observe an existing chess game", GAME_ID_ARG);


    private final String cmdString;
    private final String cmdDescription;
    private final Collection<String> arguments;


    Commands(String cmdString, String cmdDescription, String... arguments) {
      this.cmdString = cmdString;
      this.cmdDescription = cmdDescription;
      this.arguments = new ArrayList<>();
      Collections.addAll(this.arguments, arguments);
    }

    public String getCmdString() {
      return cmdString;
    }

    @Override
    public String toString() {

      var sb = new StringBuilder();

      sb.append(consoleDraw.boldString(cmdString));
      for (var arg : arguments) {
        sb.append(" ").append(arg);
      }

      sb.append(" : ").append(consoleDraw.italicizeString(cmdDescription));
      return sb.toString();
    }

    public int getNumArguments() {
      return arguments.size();
    }
  }
}
