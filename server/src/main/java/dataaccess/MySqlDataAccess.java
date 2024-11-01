package dataaccess;

import exception.ResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess{

    public MySqlDataAccess() {
        try {
            configureDatabase();
        } catch (ResponseException | DataAccessException e) {
            System.err.println("Error configuring database: " + e.getMessage());
            // Handle the exception, or rethrow a runtime exception if appropriate
            throw new RuntimeException(e); // This will still stop execution if needed
        }
    }

    public String getPassword(String username){
        return "";
        //TODO
    }

    public void putToken(String username, String authToken){
        //TODO
    }

    public void addUser(String username, String password){
        //TODO
    }

    public void clearData(){
        //TODO
    }

    public String getUsername(String authToken){
        return "";
        //TODO
    }

    public void deleteToken(String authToken){
        //TODO
    }

    public List<Map<String, String>> listGames(){
        return new ArrayList<>();
        //TODO
    }

    public String addGame(String gameName){
        return "";
        //TODO
    }

    public Map<String, String> getGame(String gameID){
        Map<String, String> map = new HashMap<>();
        return map;
        //TODO
    }

    public void setBlackTeam(String username, String gameID){
        //TODO
    }

    public void setWhiteTeam(String username, String gameID){
        //TODO
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  chessGames (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) TEXT DEFAULT NULL,
              `blackUsername` varchar(256) TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            CREATE TABLE IF NOT EXISTS  chessUsers (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            CREATE TABLE IF NOT EXISTS  chessAuth (
              `authToken` int NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
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
