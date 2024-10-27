package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.ResponseException;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    static final ChessService service = new ChessService();

    @BeforeEach
    void clear() throws ResponseException {
        service.clearData();
    }

    @Test
    void successfulLogin() throws ResponseException {
        service.addUser("username", "123");
        assertTrue(service.isValidLogin("username", "123"));
    }

    @Test
    void invalidLogin() throws ResponseException {
        service.addUser("username", "123");
        assertFalse(service.isValidLogin("username", "1234"));
    }

    @Test
    void createToken() throws ResponseException {
        String token = service.generateToken("black");
        assertFalse(service.invalidToken(token));
    }

    @Test
    void badToken() throws ResponseException {
        String token = service.generateToken("black");
        assertTrue(service.invalidToken("black"));
    }

    @Test
    void normalLogin() throws ResponseException {
        service.addUser("Jimmy", "John");
        assertTrue(service.userExists("Jimmy"));
    }

    @Test
    void badLogin() throws ResponseException {
        service.addUser("Jimmy", "John");
        assertFalse(service.userExists("John"));
    }

    @Test
    void addingUser() throws ResponseException {
        service.addUser("LeBron", "James");
        assertTrue(service.userExists("LeBron"));
    }

    @Test
    void addingBadUser() throws ResponseException {
        service.addUser("LeBron", "James");
        assertFalse(service.userExists("James"));
    }

    @Test
    void clearData() throws ResponseException {
        service.addUser("LeBron", "James");
        service.addUser("Jimmy", "John");
        service.addGame("newGame");
        service.clearData();
        List<Map<String, String>> gamesList = service.listGames();
        assertEquals(0, gamesList.size());
    }

    @Test
    void badClearData() throws ResponseException {
        service.addUser("LeBron", "James");
        service.addUser("Jimmy", "John");
        service.addGame("newGame");
        service.clearData();
        List<Map<String, String>> gamesList = service.listGames();
        assertFalse(!gamesList.isEmpty());
    }

    @Test
    void deleteToken() throws ResponseException {
        service.addUser("LeBron", "James");
        String authToken = service.generateToken("LeBron");
        service.addUser("Jimmy", "John");
        String authToken1 = service.generateToken("Jimmy");
    }

}
