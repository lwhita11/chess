package dataaccess;
import model.UserData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataAccess {
    public String getPassword(String username);
    public void putToken(String username, String authToken);
    public void addUser(String username, String password);
    public void clearData();
    public String getUsername(String authToken);
    public void deleteToken(String authToken);
    public List<Map<String, String>> listGames();
    public String addGame(String gameName);
}


