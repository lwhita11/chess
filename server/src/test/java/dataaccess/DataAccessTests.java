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


}
