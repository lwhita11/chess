package server;

import com.google.gson.Gson;
import spark.*;

import java.util.HashMap;
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
        response.put("username", username);
        response.put("authToken", authToken);
        res.status(200);
        return new Gson().toJson(response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
