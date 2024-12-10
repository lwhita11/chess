package dataaccess;
import chess.ChessGame;
import exception.ResponseException;
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
    public List<Map<String, Object>> listGames();
    public String addGame(String gameName);
    public Map<String, Object> getGame(String gameID);
    public void setBlackTeam(String username, String gameID);
    public void setWhiteTeam(String username, String gameID);
    public void updateGame(String gameID, ChessGame game);
    public void removeBlackTeam(String gameID);
    public void removeWhiteTeam(String gameID);
}


