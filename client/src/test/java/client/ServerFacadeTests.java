package client;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.List;
import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    private static String serverUrl = "http://localhost:8080";
    private static ServerFacade serverFacade = new ServerFacade(serverUrl);

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        serverFacade.clear();
        server.stop();
    }


    @Test
    public void goodLogin() throws ResponseException {
        serverFacade.registerUser("test1", "1234", "test@mail.com");
        ServerFacade.LoginResponse response =  serverFacade.login("test1", "1234");
        Assertions.assertEquals(response.getUsername(), "test1");
    }
    @Test
    public void badLogin() throws ResponseException {
        serverFacade.registerUser("test2", "1234", "test@mail.com");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("testtwo", "1234"));
    }

    @Test
    public void goodRegister() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test3", "1234",
                "test@mail.com");
        Assertions.assertEquals(response.getUsername(), "test3");
    }
    @Test
    public void badRegister() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test4", "1234",
                "test@mail.com");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.registerUser("test4",
                        "test", "test@mail.com"));
    }

    @Test
    public void goodLogout() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test5", "test",
                "z@mail.com");
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.getAuthToken()));
    }
    @Test
    public void badLogout() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test6", "test",
                "z@mail.com");
        serverFacade.logout(response.getAuthToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(response.getAuthToken()));
    }

    @Test
    public void goodList() throws ResponseException {
        serverFacade.clear();
        ServerFacade.LoginResponse response = serverFacade.registerUser("test7", "1234",
                "test@mail.com");
        serverFacade.createGame("test1", response.getAuthToken());
        serverFacade.createGame("test2", response.getAuthToken());
        serverFacade.createGame("test3", response.getAuthToken());
        List<Map<String, Object>> games = serverFacade.listGames(response.getAuthToken());
        Assertions.assertEquals(games.size(), 3);
    }
    @Test
    public void badList() throws ResponseException {
        serverFacade.clear();
        ServerFacade.LoginResponse response = serverFacade.registerUser("test8", "1234",
                "test@mail.com");
        List<Map<String, Object>> games = serverFacade.listGames(response.getAuthToken());
        Assertions.assertEquals(games.size(), 0);
        serverFacade.createGame("test1", response.getAuthToken());
        serverFacade.createGame("test2", response.getAuthToken());
        serverFacade.createGame("test3", response.getAuthToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames("bad authToken"));
    }

    @Test
    public void goodCreate() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test9", "1234",
                "test@mail.com");
        String gameID = serverFacade.createGame("test1", response.getAuthToken());
        Assertions.assertNotNull(gameID);
    }
    @Test
    public void badCreate() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test10", "1234",
                "test@mail.com");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame("test1",
                "Not a real auth Token"));
    }

    @Test
    public void goodJoin() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test11", "1234",
                "test@mail.com");
        String gameID = serverFacade.createGame("test1", response.getAuthToken());
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(gameID, ChessGame.TeamColor.BLACK,
                response.getAuthToken()));
    }
    @Test
    public void badJoin() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test12", "1234",
                "test@mail.com");
        String gameID = serverFacade.createGame("test1", response.getAuthToken());
        ChessGame game = serverFacade.joinGame(gameID, ChessGame.TeamColor.BLACK, response.getAuthToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameID, ChessGame.TeamColor.BLACK,
                response.getAuthToken()));
    }

    @Test
    public void goodObserve() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test13", "1234",
                "test@mail.com");
        String gameID = serverFacade.createGame("test1", response.getAuthToken());
        ChessGame game = serverFacade.observeGame(gameID, response.getAuthToken());
        ChessGame sameGame = serverFacade.observeGame(gameID, response.getAuthToken());
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(gameID, ChessGame.TeamColor.BLACK,
                response.getAuthToken()));
    }
    @Test
    public void badObserve() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test14", "1234",
                "test@mail.com");
        String gameID = serverFacade.createGame("test1", response.getAuthToken());
        ChessGame game = serverFacade.observeGame(gameID, response.getAuthToken());
        ChessGame sameGame = serverFacade.observeGame(gameID, response.getAuthToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.observeGame(gameID,
                "bad auth token"));
    }

}
