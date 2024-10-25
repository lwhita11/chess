package dataaccess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{
    HashMap<String, String> passwords = new HashMap<>();
    HashMap<String, String> authTokens = new HashMap<>();
    List<Map<String, String>> games = new ArrayList<>();
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
        //remove games
    }

    public String getUsername(String authToken) {
        return authTokens.get(authToken);
    }

    public void deleteToken(String authToken) {
        authTokens.remove(authToken);
    }

    public List<Map<String, String>> listGames() {
        return games;
    }

    public String addGame(String gameName) {
        String thisId = Integer.toString(gameID);
        gameID++;
        HashMap<String, String> thisGame = new HashMap<>();
        thisGame.put("gameID", thisId);
        thisGame.put("whiteUsername", null);
        thisGame.put("blackUsername", null);
        thisGame.put("gameName", gameName);
        games.add(thisGame);
        return thisId;
    }
}
