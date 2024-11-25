package dataaccess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{
    HashMap<String, String> passwords = new HashMap<>();
    HashMap<String, String> authTokens = new HashMap<>();
    List<Map<String, Object>> gamesList = new ArrayList<>();
    Map<String, Map<String, Object>> gamesMap = new HashMap<>();
    int gameID = 1;

    public String getPassword(String username) {
        return passwords.get(username);
    }

    public void putToken(String username, String authToken) {
        authTokens.put(authToken, username);
    }

    public void addUser(String username, String password) {
        passwords.put(username, password);
    }

    public void clearData() {
        passwords.clear();
        authTokens.clear();
        gamesMap.clear();
        gamesList.clear();
    }

    public String getUsername(String authToken) {
        return authTokens.get(authToken);
    }

    public void deleteToken(String authToken) {
        authTokens.remove(authToken);
    }

    public List<Map<String, Object>> listGames() {
        return gamesList;
    }

    public String addGame(String gameName) {
        String thisId = Integer.toString(gameID);
        gameID++;
        HashMap<String, Object> thisGame = new HashMap<>();
        thisGame.put("gameID", thisId);
        thisGame.put("whiteUsername", null);
        thisGame.put("blackUsername", null);
        thisGame.put("gameName", gameName);
        gamesList.add(thisGame);
        gamesMap.put(thisId, thisGame);
        return thisId;
    }

    public Map<String, Object> getGame(String gameID) {
        return gamesMap.get(gameID);
    }

    public void setBlackTeam(String username, String gameID) {
        Map<String, Object> game = gamesMap.get(gameID);
        if (game != null) {
            game.put("blackUsername", username);
            gamesMap.put(gameID, game);
        }
    }

    public void setWhiteTeam(String username, String gameID) {
        Map<String, Object> game = gamesMap.get(gameID);
        if (game!= null) {
            game.put("whiteUsername", username);
            gamesMap.put(gameID, game);
        }
    }
}
