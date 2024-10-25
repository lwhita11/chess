package dataaccess;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    HashMap<String, String> passwords = new HashMap<>();
    HashMap<String, String> authTokens = new HashMap<>();

    public String getPassword(String username) {
        return passwords.get(username);
    }

    public void putToken(String username, String authToken) {
        authTokens.put(authToken, username);
    }
}
