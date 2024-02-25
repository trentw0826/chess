package server;

import handler.RegisterHandler;
import spark.*;

/**
 * Server class to define and run basic Spark server
 */
public class Server {

    /**
     * Runs the spark server.
     *
     * @param port  the port on which to run the spark server
     * @return      the port on which initialized
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
        Spark.delete("/db", (req, res) -> "TODO: ClearHandler.getInstance().handleRequest(req, res)");
        Spark.post("/user", (req, res) -> RegisterHandler.getInstance().handleRequest(req, res));
        Spark.post("/session", (req, res) -> "TODO: LoginHandler().getInstance().handleRequest(req, res)");
        Spark.delete("/session", (req, res) -> "TODO: new LogoutHandler().getInstance().handleRequest(req, res)");
        Spark.get("/game", (req, res) -> "TODO: ListGameHandler().getInstance().handleRequest(req, res)");
        Spark.post("/game", (req, res) -> "TODO: CreateGameHandler().getInstance().handleRequest(req, res)");
        Spark.put("/game", (req, res) -> "TODO: JoinGameHandler().getInstance().handleRequest(req, res)");
    }
}
