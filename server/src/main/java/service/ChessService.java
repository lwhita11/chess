package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChessService {
    private DataAccess dataAccess;

    public ChessService() {
        dataAccess = new MySqlDataAccess();
    }

    public boolean isValidLogin(String username, String password) {
        var dbPassword = dataAccess.getPassword(username); //hashed password
        if (dbPassword == null) {
            System.err.println("User does not exist: " + username);
            return false;
        }
        return BCrypt.checkpw(password, dbPassword); //Compare clear text password to hashed password from db
    }

    public String generateToken(String username) {
        String authToken = UUID.randomUUID().toString();
        putToken(username, authToken);
        return authToken;
    }

    private void putToken(String username, String authToken) {
        dataAccess.putToken(username, authToken);
    }

    public void addUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        dataAccess.addUser(username, hashedPassword);
    }

    public boolean userExists(String username){
        if (dataAccess.getPassword(username) == null) {
            return false;
        }
        return true;
    }

    public boolean invalidToken(String authToken) {
        if (dataAccess.getUsername(authToken) == null) {
            return true;
        }
        return false;
    }

    public void clearData(){
        dataAccess.clearData();
    }

    public void deleteToken(String authToken) {
        dataAccess.deleteToken(authToken);
    }

    public List<Map<String, Object>> listGames(){
        return dataAccess.listGames();
    }

    public String addGame(String gameName){
        return dataAccess.addGame(gameName);
    }

    public boolean teamIsTaken(String gameID, ChessGame.TeamColor playerColor) {
        Map<String, Object> game = dataAccess.getGame(gameID);
        if (game == null) {
            return true;
        }
        String teamColor = "";
        if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
            teamColor = "blackUsername";
        }
        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            teamColor = "whiteUsername";
        }
        if (game.get(teamColor) == null){
            return false;
        }
        return true;
    }

    public ChessGame getGame(String gameID) {
        if (invalidID(gameID)) {
            return null;
        }
        return (ChessGame) dataAccess.getGame(gameID).get("chessGame");
    }

    public ChessGame setWhiteTeam(String authToken, String gameID) {
        if (!teamIsTaken(gameID, ChessGame.TeamColor.WHITE)) {
            String username = dataAccess.getUsername(authToken);
            dataAccess.setWhiteTeam(username, gameID);
        }
        if (invalidID(gameID)) {
            return null;
        }
        return (ChessGame) dataAccess.getGame(gameID).get("chessGame");
    }
    public Object setBlackTeam(String authToken, String gameID) {
        if (!teamIsTaken(gameID, ChessGame.TeamColor.BLACK)) {
            String username = dataAccess.getUsername(authToken);
            dataAccess.setBlackTeam(username, gameID);
        }
        if (invalidID(gameID)) {
            return null;
        }
        ChessGame chessGame = (ChessGame) dataAccess.getGame(gameID).get("chessGame");
        return chessGame;
    }

    public boolean invalidID(String gameID) {
        if (dataAccess.getGame(gameID) == null) {
            return true;
        }
        return false;
    }

    private String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public ChessBoard makeMove(String gameID, String authToken, ChessMove move) {
        if (invalidToken(authToken)) {
            return null;
        }
        if (invalidID(gameID)) {
            return null;
        }
        ChessGame currGame = getGame(gameID);
        try {
            currGame.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        dataAccess.updateGame(gameID, currGame);
        return currGame.getBoard();
    }

    public String getUsername(String authToken) {
        if (invalidToken(authToken)) {
            return null;
        }
        return dataAccess.getUsername(authToken);
    }

    public Map<String, Object> getGameMap(String gameID) {
        return dataAccess.getGame(gameID);
    }

    public void updateGame(String gameID, ChessGame game) {
        dataAccess.updateGame(gameID, game);
    }

    public void removeWhiteTeam(String gameID) {
        dataAccess.removeWhiteTeam(gameID);
    }
    public void removeBlackTeam(String gameID) {
        dataAccess.removeBlackTeam(gameID);
    }

}
