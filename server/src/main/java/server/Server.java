package server;

import handler.*;
import httpPath.HttpPath;
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
        Spark.delete(HttpPath.PATHS.DB.getPath(), (req, res) -> ClearHandler.instance().handleRequest(req, res));
        Spark.post(HttpPath.PATHS.USER.getPath(), (req, res) -> RegisterHandler.instance().handleRequest(req, res));
        Spark.post(HttpPath.PATHS.SESSION.getPath(), (req, res) -> LoginHandler.instance().handleRequest(req, res));
        Spark.delete(HttpPath.PATHS.SESSION.getPath(), (req, res) -> LogoutHandler.instance().handleRequest(req, res));
        Spark.post(HttpPath.PATHS.GAME.getPath(), (req, res) -> CreateGameHandler.instance().handleRequest(req, res));
        Spark.get(HttpPath.PATHS.GAME.getPath(), (req, res) -> ListGamesHandler.instance().handleRequest(req, res));
        Spark.put(HttpPath.PATHS.GAME.getPath(), (req, res) -> JoinGameHandler.instance().handleRequest(req, res));
    }
}
