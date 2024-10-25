package dataaccess;
import model.UserData;

public interface DataAccess {
    public String getPassword(String username);
    public void putToken(String username, String authToken);
}


