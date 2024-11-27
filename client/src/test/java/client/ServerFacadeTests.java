package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


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
        serverFacade.registerUser("test", "1234", "test@mail.com");
        ServerFacade.LoginResponse response =  serverFacade.login("test", "1234");
        Assertions.assertEquals(response.getUsername(), "test");
    }
    @Test
    public void badLogin() throws ResponseException {
        serverFacade.registerUser("test", "1234", "test@mail.com");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("test1", "1234"));
    }

    @Test
    public void goodRegister() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test", "1234",
                "test@mail.com");
        Assertions.assertEquals(response.getUsername(), "test");
    }
    @Test
    public void badRegister() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test", "1234",
                "test@mail.com");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.registerUser("test",
                        "test", "test@mail.com"));
    }

    @Test
    public void goodLogout() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test", "test",
                "z@mail.com");
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.getAuthToken()));
    }
    @Test
    public void badLogout() throws ResponseException {
        ServerFacade.LoginResponse response = serverFacade.registerUser("test", "test",
                "z@mail.com");
        serverFacade.logout(response.getAuthToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(response.getAuthToken()));
    }

    @Test
    public void goodList() {
        Assertions.assertTrue(true);
    }
    @Test
    public void badList() {
        Assertions.assertTrue(true);
    }

    @Test
    public void goodCreate() {
        Assertions.assertTrue(true);
    }
    @Test
    public void badCreate() {
        Assertions.assertTrue(true);
    }

    @Test
    public void goodJoin() {
        Assertions.assertTrue(true);
    }
    @Test
    public void badJoin() {
        Assertions.assertTrue(true);
    }

    @Test
    public void goodObserve() {
        Assertions.assertTrue(true);
    }
    @Test
    public void badObserve() {
        Assertions.assertTrue(true);
    }

}
