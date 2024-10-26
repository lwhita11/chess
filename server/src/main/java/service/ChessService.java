package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChessService {
    private DataAccess dataAccess;

    public ChessService() {
        this.dataAccess = new MemoryDataAccess();
    }

    public boolean isValidLogin(String username, String password) {
        String dbPassword = dataAccess.getPassword(username);
        if (password.equals(dbPassword)) {
            return true;
        }
        return false;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void putToken(String username, String authToken) {
        dataAccess.putToken(username, authToken);
    }

    public void addUser(String username, String password) {
        dataAccess.addUser(username, password);
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

    public List<Map<String, String>> listGames(){
        return dataAccess.listGames();
    }

    public String addGame(String gameName){
        return dataAccess.addGame(gameName);
    }

    public boolean teamIsTaken(String gameID, ChessGame.TeamColor playerColor) {
        Map<String, String> game = dataAccess.getGame(gameID);
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

    public void setWhiteTeam(String authToken, String gameID) {
        String username = dataAccess.getUsername(authToken);
        dataAccess.setWhiteTeam(username, gameID);
    }
    public void setBlackTeam(String authToken, String gameID) {
        String username = dataAccess.getUsername(authToken);
        dataAccess.setBlackTeam(username, gameID);
    }

    public boolean invalidID(String gameID) {
        if (dataAccess.getGame(gameID) == null) {
            return true;
        }
        return false;
    }
}
