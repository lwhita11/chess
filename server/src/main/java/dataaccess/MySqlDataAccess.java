package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlDataAccess implements DataAccess{
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
}
