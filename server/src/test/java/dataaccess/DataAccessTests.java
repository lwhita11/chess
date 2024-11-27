package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.ChessService;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;


public class DataAccessTests {
    static final MySqlDataAccess DATA_ACCESS = new MySqlDataAccess();

    @BeforeEach
    void clear() throws ResponseException {
        DATA_ACCESS.clearData();
    }

    @Test
    void addUser() throws ResponseException {
        DATA_ACCESS.addUser("User", "Name");
        String password = DATA_ACCESS.getPassword("User");
        assertEquals(password, "Name");
    }

    @Test
    void badAddUser() throws ResponseException {
        DATA_ACCESS.addUser(null, "Name");
        String password = DATA_ACCESS.getPassword(null);
        assertNotEquals("Name", password);
    }

    @Test
    void getPassword() throws ResponseException {
        DATA_ACCESS.addUser("Name", "Name");
        String password = DATA_ACCESS.getPassword("Name");
        assertEquals("Name", password);
    }

    @Test
    void getPasswordBadUsername() throws ResponseException {
        DATA_ACCESS.addUser("Name", "Name");
        String password = DATA_ACCESS.getPassword("User");
        assertNull(password);
    }

    @Test
    void putToken() throws ResponseException {
        DATA_ACCESS.putToken("test", "token");
        String username = DATA_ACCESS.getUsername("token");
        assertEquals(username, "test");
    }

    @Test
    void putBadToken() throws ResponseException {
        DATA_ACCESS.putToken("test", null);
        String username = DATA_ACCESS.getUsername(null);
        assertNotEquals(username, "test");
    }

    @Test
    void getUsername() throws ResponseException {
        DATA_ACCESS.putToken("Username", "token");
        String username = DATA_ACCESS.getUsername("token");
        assertEquals(username, "Username");
    }

    @Test
    void badGetUsername() throws ResponseException {
        DATA_ACCESS.putToken("test", "Token");
        String username = DATA_ACCESS.getUsername("NotaToken");
        assertNull(username);
    }

    @Test
    void deleteToken() throws ResponseException {
        DATA_ACCESS.putToken("test", "Token");
        String username = DATA_ACCESS.getUsername("Token");
        assertEquals(username, "test");
        DATA_ACCESS.deleteToken("Token");
        String username2 = DATA_ACCESS.getUsername("Token");
        assertNull(username2);
    }

    @Test
    void badDeleteToken() throws ResponseException {
        DATA_ACCESS.putToken("test", "Token");
        String username = DATA_ACCESS.getUsername("Token");
        assertEquals(username, "test");
        DATA_ACCESS.deleteToken("NotAToken");
        String username2 = DATA_ACCESS.getUsername("Token");
        assertEquals(username2, username);
    }

    @Test
    void addGame() throws ResponseException {
        String gameID = DATA_ACCESS.addGame("testGame");
        Map<String, Object> game = DATA_ACCESS.getGame(gameID);
        assertEquals(game.get("gameID"), gameID);
    }

    @Test
    void addNullGame() throws ResponseException {
        String gameID = DATA_ACCESS.addGame(null);
        Map<String, Object> game = DATA_ACCESS.getGame(gameID);
        assertNull(gameID);
        assertNull(game);
    }

    @Test
    void getGame() throws ResponseException {
        String gameID = DATA_ACCESS.addGame("NewGame");
        Map<String, Object> game = DATA_ACCESS.getGame(gameID);
        assertEquals("NewGame", game.get("gameName"));
    }

    @Test
    void getBadGame() throws ResponseException {
        String gameID = DATA_ACCESS.addGame("MyGame");
        assertThrows(NumberFormatException.class, () -> DATA_ACCESS.getGame("IncorrectGameID"));
    }

    @Test
    void listGames() throws ResponseException {
        String gameID1 = DATA_ACCESS.addGame("game1");
        String gameID2 = DATA_ACCESS.addGame("game2");
        List<Map<String, Object>> games = DATA_ACCESS.listGames();
        assertEquals(games.size(), 2);
    }

    @Test
    void listBadGames() throws ResponseException {
        String gameID1 = DATA_ACCESS.addGame(null);
        String gameID2 = DATA_ACCESS.addGame("game1");
        List<Map<String, Object>> games = DATA_ACCESS.listGames();
        List<Map<String, Object>> expectedGames = new ArrayList<>();
        expectedGames.add(DATA_ACCESS.getGame(gameID1));
        expectedGames.add(DATA_ACCESS.getGame(gameID2));
        assertNotEquals(games, expectedGames);
        expectedGames.remove(null);
        assertEquals(games.size(), 1);
    }

    @Test
    void setBlackUsername() throws ResponseException {
    String gameID1 = DATA_ACCESS.addGame("game1");
    DATA_ACCESS.setBlackTeam("Test", gameID1);
    Map<String, Object> game1 = DATA_ACCESS.getGame(gameID1);
    assertEquals(game1.get("blackUsername"), "Test");
    }

    @Test
    void setBadBlackUsername() throws ResponseException {
        String gameID = DATA_ACCESS.addGame("NewGame");
        assertThrows(NumberFormatException.class, () -> DATA_ACCESS.getGame("IncorrectGameID"));
        DATA_ACCESS.setBlackTeam("Test", "88"); //different ID number
        Map<String, Object> game = DATA_ACCESS.getGame(gameID);
        assertNotEquals(game.get("blackUsername"), "Test");
    }

    @Test
    void setWhiteUsername() throws ResponseException {
        String gameID1 = DATA_ACCESS.addGame("game");
        DATA_ACCESS.setWhiteTeam("Test", gameID1);
        Map<String, Object> game1 = DATA_ACCESS.getGame(gameID1);
        assertEquals(game1.get("whiteUsername"), "Test");
    }

    @Test
    void setBadWhiteUsername() throws ResponseException {
        String gameID = DATA_ACCESS.addGame("Game1");
        assertThrows(NumberFormatException.class, () -> DATA_ACCESS.getGame("IncorrectGameID"));
        DATA_ACCESS.setWhiteTeam("Test", "88"); //different ID number
        Map<String, Object> game = DATA_ACCESS.getGame(gameID);
        assertNotEquals(game.get("whiteUsername"), "Test");
    }



}
