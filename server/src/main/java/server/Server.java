package server;

import com.google.gson.Gson;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import service.ChessService;

public class Server {
    private ChessService service;

    public Server() {
        this.service = new ChessService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::login);
        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    //Login endpoint
    private Object login(Request req, Response res){
        Map<String, String> loginData = new Gson().fromJson(req.body(), HashMap.class);
        String username = loginData.get("username");
        String password = loginData.get("password");
        if (!service.isValidLogin(username, password)) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        String authToken = service.generateToken();
        service.putToken(username, authToken);

        Map<String, String> response = new HashMap<>();
        response.put("authToken", authToken);
        response.put("username", username);
        res.status(200);
        return new Gson().toJson(response);
    }

    private Object register(Request req, Response res) {
        Map<String, String> registerData = new Gson().fromJson(req.body(), HashMap.class);
        String username = registerData.get("username");
        String password = registerData.get("password");
        String email = registerData.get("email");
        if (service.userExists(username)) {
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        }
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        service.addUser(username, password);
        String authToken = service.generateToken();
        service.putToken(username, authToken);

        Map<String, String> response = new HashMap<>();
        response.put("authToken", authToken);
        response.put("username", username);
        res.status(200);
        return new Gson().toJson(response);
    }

    private Object clear(Request req, Response res) {
        service.clearData();
        res.status(200);
        Map<String, String> response = new HashMap<>();
        return new Gson().toJson(response);
    }

    private Object logout(Request req, Response res) {
        String authToken = req.headers("authorization");
        if (service.invalidToken(authToken)) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        service.deleteToken(authToken);

        Map<String, String> response = new HashMap<>();
        res.status(200);
        return new Gson().toJson(response);
    }

    private Object listGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        if (service.invalidToken(authToken)) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        List<Map<String, String>> games = service.listGames();
        Map<String, Object> response = new HashMap<>();
        res.status(200);
        response.put("games", games);
        return new Gson().toJson(response);
    }

    private Object createGame(Request req, Response res){
        Map<String, String> createGameData = new Gson().fromJson(req.body(), HashMap.class);
        String gameName = createGameData.get("gameName");
        String authToken = req.headers("authorization");
        if (service.invalidToken(authToken)) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        if (gameName.isEmpty()) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        String gameID = service.addGame(gameName);
        res.status(200);
        Map<String, String> response = new HashMap<>();
        response.put("gameID", gameID);
        return new Gson().toJson(response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
