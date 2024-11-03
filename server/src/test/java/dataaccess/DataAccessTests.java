package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.ChessService;

import java.lang.reflect.Method;
import java.sql.*;
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



}
