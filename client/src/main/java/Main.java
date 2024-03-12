import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

import static java.lang.System.exit;


/**
 * Class interacts with the client, both processing input and displaying output on the console
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Enum class to contain command types. Includes member variables to hold each command as a string
     * as well as its desription
     */
    enum Command {
        HELP("help", "display available commands"),
        QUIT("quit", "exit the program"),
        LOGIN("login", "log in to your account"),
        REGISTER("register", "create a new account"),
        LOGOUT("logout" ,"log out from your account"),
        CREATE_GAME("create", "create a new chess game"),
        LIST_GAMES("list", "list all available chess games"),
        JOIN_GAME("join", "join an existing chess game"),
        JOIN_OBSERVER("observe", "observe an existing chess game");

        private final String commandString;
        private final String description;

        Command(String commandString, String description) {
            this.commandString = commandString;
            this.description = description;
        }
    }

    // Set of before-login commands
    private static final Collection<Command> PRE_LOGIN_COMMANDS = Set.of(
            Command.HELP,
            Command.QUIT,
            Command.LOGIN,
            Command.REGISTER
    );

    // Set of after-login commands
    private static final Collection<Command> POST_LOGIN_COMMANDS = Set.of(
            Command.HELP,
            Command.LOGOUT,
            Command.CREATE_GAME,
            Command.LIST_GAMES,
            Command.JOIN_GAME,
            Command.JOIN_OBSERVER
    );

    /*
     * Holds the set of available commands for the client at any given moment
     */
    private static Collection<Command> availableCommands;

    public static void main(String[] args) {
        String userInput;
        availableCommands = PRE_LOGIN_COMMANDS;

        displayWelcomeMessage();

        while (true) {
            userInput = getValidUserInput();
            Command userCommand = retrieveCommand(userInput);
            processCommand(userCommand);
        }
    }


    /**
     * Given an available command in the form of a string, returns the associated command.
     *
     * @param commandString command in string form
     * @return              command associated with 'commandString'
     */
    private static Command retrieveCommand(String commandString) {
        for (Command command : availableCommands) {
            if (command.commandString.equalsIgnoreCase(commandString)) {
                return command;
            }
        }
        // String doesn't match an available command
        throw new IllegalArgumentException("Invalid command: " + commandString);
    }


    /**
     * Given a valid command, call associated program methods (as defined in phase 5 specs).
     *
     * @param command a valid user input
     */
    private static void processCommand(Command command) {
        switch (command) {
            case Command.HELP:
                displayAvailableCommands();
                break;
            case Command.QUIT:
                exitProgram();
                break;
            case Command.LOGIN:
                System.out.println("Logging in...");
                availableCommands = POST_LOGIN_COMMANDS;
                // Implement login functionality
                break;
            case Command.REGISTER:
                System.out.println("Registering...");
                // Implement registration functionality
                break;
            case Command.LOGOUT:
                System.out.println("Logging out...");
                availableCommands = PRE_LOGIN_COMMANDS;
                // Implement logout functionality
                break;
            case Command.CREATE_GAME:
                System.out.println("Creating a new game...");
                // Implement create game functionality
                break;
            case Command.LIST_GAMES:
                System.out.println("Listing available games...");
                // Implement list games functionality
                break;
            case Command.JOIN_GAME:
                System.out.println("Joining a game...");
                // Implement join game functionality
                break;
            case Command.JOIN_OBSERVER:
                System.out.println("Observing a game...");
                // Implement join observer functionality
                break;
            default:
                throw new IllegalArgumentException("Illegal command passed");
        }
    }


    /**
     * Terminate the program with success
     */
    private static void exitProgram() {
        System.out.println("Exiting program...");
        exit(0);
    }


    /**
     * Get a valid user input from the command line.
     * A valid input is defined as a string that, when trimmed and lower-cased,
     * matches one of the available commands in the 'availableCommands' set
     *
     * @return  validated command line input
     */
    private static String getValidUserInput() {
        String userInput;
        boolean inputValidated = false;

        // TODO how to not repeat code in the following blocks?
        displayUserInputPrompt();
        userInput = SCANNER.nextLine().trim().toLowerCase();
        inputValidated = validateUserInput(userInput);

        while(!inputValidated) {
            displayAssistedUserInputPrompt(userInput);
            userInput = SCANNER.nextLine().trim().toLowerCase();
            inputValidated = validateUserInput(userInput);
        }

        return userInput;
    }


    /**
     * Given a string entered on the command line, return if that string
     * corresponds to a currently available command.
     *
     * @param userInput command line input string
     * @return          if 'userInput' corresponds to some 'commandStr' in the 'Command' enum class
     */
    private static boolean validateUserInput(String userInput) {
        for (var command : availableCommands) {
            if (command.commandString.equalsIgnoreCase(userInput)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Responds to 'help command', displays all commands currently held in
     * the 'availableCommands' collection.
     */
    private static void displayAvailableCommands() {
        StringBuilder sb = new StringBuilder();

        for (var command : availableCommands) {
            sb.append(" '").append(command.commandString).append("'").append(" : ").append(command.description).append(System.lineSeparator());
        }

        System.out.print(sb.toString());
    }


    /*
     * Helper display methods
     */
    private static void displayWelcomeMessage() {
        System.out.println("welcome to the chess client!");
    }

    private static void displayUserInputPrompt() {
        System.out.print(">>> ");
    }

    private static void displayAssistedUserInputPrompt(String userInput) {
        System.out.printf("'%s' was not recognized as a command ('%s' for available commands)%n", userInput, Command.HELP.commandString);
        displayUserInputPrompt();
    }
}
