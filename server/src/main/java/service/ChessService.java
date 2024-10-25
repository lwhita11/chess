package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;

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
}
