package server;

import handler.RegisterHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register endpoints
        createRoutes();
        // TODO: handle exceptions

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private static void createRoutes() {
        Spark.delete("/db", (req, res) -> "TODO: ClearHandler().getInstance().handleRequest(req, res)");                            // clear
        Spark.post("/user", (req, res) -> RegisterHandler.getInstance().handleRequest(req, res));
        Spark.post("/session", (req, res) -> "TODO: LoginHandler().getInstance().handleRequest(req, res)");
        Spark.delete("/session", (req, res) -> "TODO: new LogoutHandler().getInstance().handleRequest(req, res)");
        Spark.get("/game", (req, res) -> "TODO: ListGameHandler().getInstance().handleRequest(req, res)");
        Spark.post("/game", (req, res) -> "TODO: CreateGameHandler().getInstance().handleRequest(req, res)");
        Spark.put("/game", (req, res) -> "TODO: JoinGameHandler().getInstance().handleRequest(req, res)");
    }
}
