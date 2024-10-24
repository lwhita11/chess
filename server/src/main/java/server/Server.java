package server;

import spark.*;
import java.util.UUID;

public class Server {

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

    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
