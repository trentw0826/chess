package server;

import handler.*;
import spark.*;

/**
 * Server class to define and run basic Spark server.
 */
public class Server {

    /**
     * Runs the spark server.
     *
     * @param port  the port on which to run the spark server
     * @return      the initialized port number
     */
    public int run(int port) {
        Spark.port(port);

        Spark.staticFiles.location("web");

        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Stops the spark server.
     */
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Defines the spark server's routes.
     */
    private static void createRoutes() {

        // Contains enum values associated with needed path strings
        enum PATHS {
            DB("/db"),
            USER("/user"),
            SESSION("/session"),
            GAME("/game");

            private final String path;

            PATHS(String path) {
                this.path = path;
            }

            public String path() {
                return path;
            }
        }

        Spark.delete(PATHS.DB.path, (req, res) -> ClearHandler.instance().handleRequest(req, res));
        Spark.post(PATHS.USER.path, (req, res) -> RegisterHandler.instance().handleRequest(req, res));
        Spark.post(PATHS.SESSION.path, (req, res) -> LoginHandler.instance().handleRequest(req, res));
        Spark.delete(PATHS.SESSION.path, (req, res) -> LogoutHandler.instance().handleRequest(req, res));
        Spark.post(PATHS.GAME.path, (req, res) -> CreateGameHandler.instance().handleRequest(req, res));
        Spark.get(PATHS.GAME.path, (req, res) -> ListGamesHandler.instance().handleRequest(req, res));
        Spark.put(PATHS.GAME.path, (req, res) -> JoinGameHandler.instance().handleRequest(req, res));
    }
}
