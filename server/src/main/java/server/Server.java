package server;

import handler.*;
import spark.*;

/**
 * Server class to define and run basic Spark server
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
     * Stops the spark server
     */
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Defines the spark server's routes
     */
    private static void createRoutes() {
        Spark.delete("/db", (req, res) -> ClearHandler.getInstance().handleHttpRequest(req, res));
        Spark.post("/user", (req, res) -> RegisterHandler.getInstance().handleHttpRequest(req, res));
        Spark.post("/session", (req, res) -> LoginHandler.getInstance().handleHttpRequest(req, res));
        Spark.delete("/session", (req, res) -> LogoutHandler.getInstance().handleHttpRequest(req, res));
        Spark.post("/game", (req, res) -> CreateGameHandler.getInstance().handleHttpRequest(req, res));
        Spark.get("/game", (req, res) -> "TODO: ListGameHandler.getInstance().handleRequest(req, res)");
        Spark.put("/game", (req, res) -> "TODO: JoinGameHandler.getInstance().handleRequest(req, res)");
    }
}
