package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.ResponseException;
import spark.Response;

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
        service.deleteToken(authToken);
        assertTrue(service.invalidToken(authToken));
    }

    @Test
    void deleteWrongToken() throws ResponseException {
        service.addUser("LeBron", "James");
        String authToken = service.generateToken("LeBron");
        service.addUser("Jimmy", "John");
        String authToken1 = service.generateToken("Jimmy");
        service.deleteToken("Hahahaha");
        assertFalse(service.invalidToken(authToken));
    }

    @Test
    void listGames() throws ResponseException {
        service.addGame("test game");
        service.addGame("test game2");
        List<Map<String, String>> games = service.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listAfterClear() throws ResponseException {
        service.addGame("test game");
        service.addGame("test game2");
        service.clearData();
        List<Map<String, String>> games = service.listGames();
        assertEquals(0, games.size());
    }

    @Test
    void addGame() throws ResponseException {
        service.addGame("test game");
        List<Map<String, String>> games = service.listGames();
        assertEquals(1, games.size());
    }

    @Test
    void addBadGame() throws ResponseException {
        service.addGame("test game");
        service.addGame("test game2");
        List<Map<String, String>> games = service.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void setBlack() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        service.setBlackTeam(authToken, gameID);
        List<Map<String, String>> games = service.listGames();
        assertTrue(service.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
    }

    @Test
    void badSetBlack() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        service.setBlackTeam(authToken, "1234");
        service.setBlackTeam("badToken", gameID);
        List<Map<String, String>> games = service.listGames();
        assertFalse(service.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
    }

    @Test
    void setWhite() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        service.setBlackTeam(authToken, gameID);
        List<Map<String, String>> games = service.listGames();
        assertTrue(service.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
    }

    @Test
    void badSetWhite() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        service.setWhiteTeam(authToken, "1234");
        service.setWhiteTeam("badToken", gameID);
        List<Map<String, String>> games = service.listGames();
        assertFalse(service.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
    }

    @Test
    void teamIsTaken() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        service.setBlackTeam(authToken, gameID);
        service.setWhiteTeam(authToken, gameID);
        List<Map<String, String>> games = service.listGames();
        assertTrue(service.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
        assertTrue(service.teamIsTaken(gameID, ChessGame.TeamColor.WHITE));
    }

    @Test
    void teamIsNotTaken() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        assertFalse(service.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
        assertFalse(service.teamIsTaken(gameID, ChessGame.TeamColor.WHITE));
    }

    @Test
    void invalidID() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        assertFalse(service.invalidID(gameID));
    }

    @Test
    void badID() throws ResponseException {
        service.addGame("test game");
        service.addUser("John", "Jones");
        String authToken = service.generateToken("John");
        String gameID = service.addGame("test game2");
        assertTrue(service.invalidID("1234"));
    }







}
