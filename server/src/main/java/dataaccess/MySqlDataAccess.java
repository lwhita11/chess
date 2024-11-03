package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess{

    public MySqlDataAccess() {
        try {
            configureDatabase();
        } catch (ResponseException | DataAccessException e) {
            System.err.println("Error configuring database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getPassword(String username) {
        var statement = "SELECT password FROM chessUsers WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                } else {
                    return null;
                }
            }

        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }

    public void putToken(String username, String authToken) {
        var statement = "INSERT INTO chessAuth (authToken, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, authToken, username);
        } catch (ResponseException ex) {
            return;
        }
    }

    public void addUser(String username, String password) {
        var statement = "INSERT INTO chessUsers (username, password) VALUES (?, ?)";
        try {
            executeUpdate(statement, username, password);
        } catch (ResponseException ex) {
            System.err.println("Error adding user: " + ex.getMessage());
        }
    }

    public void clearData(){
        var statement1 = "DROP TABLE chessGames";
        var statement2 = "DROP TABLE chessUsers";
        var statement3 = "DROP TABLE chessAuth";
        try {
            executeUpdate(statement1);
            executeUpdate(statement2);
            executeUpdate(statement3);
            configureDatabase();
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername(String authToken){
        var statement = "SELECT username FROM chessAuth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    return null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }

    public void deleteToken(String authToken){
        var statement = "DELETE FROM chessAuth WHERE authToken =?";
        try {
            executeUpdate(statement, authToken);
        } catch (ResponseException e) {
            System.err.println("Failed to delete token: " + e.getMessage());
        }
    }

    public List<Map<String, String>> listGames(){
        List<Map<String, String>> games = new ArrayList<>();
        var statement = "SELECT * FROM chessGames";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> thisGame = new HashMap<>();
                    thisGame.put("gameID", rs.getString("gameID"));
                    thisGame.put("whiteUsername", rs.getString("whiteUsername"));
                    thisGame.put("blackUsername", rs.getString("blackUsername"));
                    thisGame.put("gameName", rs.getString("gameName"));
                    games.add(thisGame);
                }
                return games;
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }

    public String addGame(String gameName) {
        ChessGame newGame = new ChessGame();
        Gson gson = new Gson();
        String jsonGame = gson.toJson(newGame);
        var statement = "INSERT INTO chessGames (gameName, whiteUsername, blackUsername, json) VALUES (?, ?, ?, ?);";
        try{
            int gameID = executeUpdate(statement, gameName, null, null, jsonGame);
            return Integer.toString(gameID);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> getGame(String gameID){
        if (gameID == null){
            return null;
        }
        int gameInt = Integer.parseInt(gameID);
        Map<String, String> map = new HashMap<>();

        var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, json FROM chessGames WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameInt);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String jsonGame = rs.getString("json");
                    map.put("gameID", gameID);
                    map.put("whiteUsername", rs.getString("whiteUsername"));
                    map.put("blackUsername", rs.getString("blackUsername"));
                    map.put("gameName", rs.getString("gameName"));
                    Gson gson = new Gson();
                    ChessGame chessGame = gson.fromJson(jsonGame, ChessGame.class);
                    return map;
                } else {
                    return null;
                }
            }

        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }

    public void setBlackTeam(String username, String gameID){
        var statement = "UPDATE chessGames SET blackUsername = ? WHERE gameID = ?";
        int gameInt = Integer.parseInt(gameID);
        try {
            executeUpdate(statement, username, gameInt);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setWhiteTeam(String username, String gameID){
        var statement = "UPDATE chessGames SET whiteUsername = ? WHERE gameID = ?";
        int gameInt = Integer.parseInt(gameID);
        try {
            executeUpdate(statement, username, gameInt);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p){
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p){
                        ps.setInt(i + 1, p);
                    }
                    else if (param instanceof ChessGame p) {
                        ps.setString(i + 1, p.toString());
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  chessGames (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUsername` TEXT DEFAULT NULL,
              `blackUsername` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS  chessUsers (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS  chessAuth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        } catch (DataAccessException ex) {
            throw new DataAccessException(String.format("Error: %s", ex.getMessage()));
        }
    }
}
