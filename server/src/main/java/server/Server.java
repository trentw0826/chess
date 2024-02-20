package server;

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
        Spark.delete("/db", (req, res) -> "TODO: call clear()");                            // clear
        Spark.post("/user", (req, res) -> "TODO: call register(username, password, email"); // register
        Spark.post("/session", (req, res) -> "TODO: call login(username, password");        // login
        Spark.delete("/session", (req, res) -> "TODO: call logout(authToken)");             // logout
        Spark.get("/game", (req, res) -> "TODO: call listGames(authToken)");                // list games
        Spark.post("/game", (req, res) -> "TODO: call createGame(authToken, gameName)");    // create game
        // TODO: Add route to join game
    }
}
