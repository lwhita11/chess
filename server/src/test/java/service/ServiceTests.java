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
    static final ChessService SERVICE = new ChessService();

    @BeforeEach
    void clear() throws ResponseException {
        SERVICE.clearData();
    }

    @Test
    void successfulLogin() throws ResponseException {
        SERVICE.addUser("username", "123");
        assertTrue(SERVICE.isValidLogin("username", "123"));
    }

    @Test
    void invalidLogin() throws ResponseException {
        SERVICE.addUser("username", "123");
        assertFalse(SERVICE.isValidLogin("username", "1234"));
    }

    @Test
    void createToken() throws ResponseException {
        String token = SERVICE.generateToken("black");
        assertFalse(SERVICE.invalidToken(token));
    }

    @Test
    void badToken() throws ResponseException {
        String token = SERVICE.generateToken("black");
        assertTrue(SERVICE.invalidToken("black"));
    }

    @Test
    void normalLogin() throws ResponseException {
        SERVICE.addUser("Jimmy", "John");
        assertTrue(SERVICE.userExists("Jimmy"));
    }

    @Test
    void badLogin() throws ResponseException {
        SERVICE.addUser("Jimmy", "John");
        assertFalse(SERVICE.userExists("John"));
    }

    @Test
    void addingUser() throws ResponseException {
        SERVICE.addUser("LeBron", "James");
        assertTrue(SERVICE.userExists("LeBron"));
    }

    @Test
    void addingBadUser() throws ResponseException {
        SERVICE.addUser("LeBron", "James");
        assertFalse(SERVICE.userExists("James"));
    }

    @Test
    void clearData() throws ResponseException {
        SERVICE.addUser("LeBron", "James");
        SERVICE.addUser("Jimmy", "John");
        SERVICE.addGame("newGame");
        SERVICE.clearData();
        List<Map<String, Object>> gamesList = SERVICE.listGames();
        assertEquals(0, gamesList.size());
    }

    @Test
    void badClearData() throws ResponseException {
        SERVICE.addUser("LeBron", "James");
        SERVICE.addUser("Jimmy", "John");
        SERVICE.addGame("newGame");
        SERVICE.clearData();
        List<Map<String, Object>> gamesList = SERVICE.listGames();
        assertFalse(!gamesList.isEmpty());
    }

    @Test
    void deleteToken() throws ResponseException {
        SERVICE.addUser("LeBron", "James");
        String authToken = SERVICE.generateToken("LeBron");
        SERVICE.addUser("Jimmy", "John");
        String authToken1 = SERVICE.generateToken("Jimmy");
        SERVICE.deleteToken(authToken);
        assertTrue(SERVICE.invalidToken(authToken));
    }

    @Test
    void deleteWrongToken() throws ResponseException {
        SERVICE.addUser("LeBron", "James");
        String authToken = SERVICE.generateToken("LeBron");
        SERVICE.addUser("Jimmy", "John");
        String authToken1 = SERVICE.generateToken("Jimmy");
        SERVICE.deleteToken("Hahahaha");
        assertFalse(SERVICE.invalidToken(authToken));
    }

    @Test
    void listGames() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addGame("test game2");
        List<Map<String, Object>> games = SERVICE.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listAfterClear() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addGame("test game2");
        SERVICE.clearData();
        List<Map<String, Object>> games = SERVICE.listGames();
        assertEquals(0, games.size());
    }

    @Test
    void addGame() throws ResponseException {
        SERVICE.addGame("test game");
        List<Map<String, Object>> games = SERVICE.listGames();
        assertEquals(1, games.size());
    }

    @Test
    void addBadGame() throws ResponseException {
        SERVICE.addGame("1game");
        SERVICE.addUser("Johnny", "James");
        List<Map<String, Object>> games = SERVICE.listGames();
        assertEquals(1, games.size());
    }

    @Test
    void setBlack() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        SERVICE.setBlackTeam(authToken, gameID);
        List<Map<String, Object>> games = SERVICE.listGames();
        assertTrue(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
    }

    @Test
    void badSetBlack() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        SERVICE.setBlackTeam(authToken, "1234");
        SERVICE.setBlackTeam("badToken", gameID);
        List<Map<String, Object>> games = SERVICE.listGames();
        assertFalse(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
    }

    @Test
    void setWhite() throws ResponseException {
        SERVICE.addGame("game3");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        SERVICE.setWhiteTeam(authToken, gameID);
        assertTrue(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.WHITE));
    }

    @Test
    void badSetWhite() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        SERVICE.setWhiteTeam(authToken, "1234");
        SERVICE.setWhiteTeam("badToken", gameID);
        List<Map<String, Object>> games = SERVICE.listGames();
        assertFalse(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.WHITE));
    }

    @Test
    void teamIsTaken() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        SERVICE.setBlackTeam(authToken, gameID);
        SERVICE.setWhiteTeam(authToken, gameID);
        List<Map<String, Object>> games = SERVICE.listGames();
        assertTrue(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
        assertTrue(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.WHITE));
    }

    @Test
    void teamIsNotTaken() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        assertFalse(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.BLACK));
        assertFalse(SERVICE.teamIsTaken(gameID, ChessGame.TeamColor.WHITE));
    }

    @Test
    void invalidID() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        assertFalse(SERVICE.invalidID(gameID));
    }

    @Test
    void badID() throws ResponseException {
        SERVICE.addGame("test game");
        SERVICE.addUser("John", "Jones");
        String authToken = SERVICE.generateToken("John");
        String gameID = SERVICE.addGame("test game2");
        assertTrue(SERVICE.invalidID("1234"));
    }







}
