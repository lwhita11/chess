package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    public static class LoginResponse {
        private String authToken;
        private String username;

        public String getAuthToken() {
            return authToken;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class GamesResponse {
        private List<Map<String, Object>> games;

        public List<Map<String, Object>> getGames() {
            return games;
        }
    }

    public static class SingleGameResponse {
        private String gameID;

        public String getGameID() {
            return gameID;
        }
    }

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    WebSocketCommunicator ws;

    public void connectWS() {
        try {
            ws = new WebsocketCommunicator(serverDomain);
        }
        catch (Exception e) {
            System.out.println("could not connect to server");
        }
    }

    public LoginResponse login(String username, String password) throws ResponseException {
        var path = "/session";
        var requestBody = Map.of("username", username, "password", password);
        return this.makeRequest("POST", path, requestBody, LoginResponse.class, null);
    }

    public LoginResponse registerUser(String username, String password, String email) throws ResponseException {
        var path = "/user";
        var requestBody = Map.of("username", username, "password", password, "email", email);
        return this.makeRequest("POST", path, requestBody, LoginResponse.class, null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        Map<String, String> headers = Map.of("authorization", authToken);
        this.makeRequest("DELETE", path, null, null, headers);
    }

    public List<Map<String, Object>> listGames(String authToken) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = Map.of("authorization", authToken);
        GamesResponse response = this.makeRequest("GET", path, null, GamesResponse.class, headers);
        return response.getGames();
    }

    public String createGame(String gameName, String authToken) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = Map.of("authorization", authToken);
        Map<String, String> requestBody = Map.of("gameName", gameName);
        SingleGameResponse response = this.makeRequest("POST", path, requestBody,
                SingleGameResponse.class, headers);
        return response.getGameID();
    }

    public ChessGame joinGame (String gameID, ChessGame.TeamColor teamColor,
                               String authToken) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = Map.of("authorization", authToken);
        Map<String, Object> requestBody = Map.of("gameID", gameID, "playerColor", teamColor);
        return this.makeRequest("PUT", path, requestBody, ChessGame.class, headers);
    }

    public ChessGame observeGame (String gameID, String authToken) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = Map.of("authorization", authToken);
        Map<String, Object> requestBody = Map.of("gameID", gameID, "playerColor", ChessGame.TeamColor.NEITHER);
        return this.makeRequest("PUT", path, requestBody, ChessGame.class, headers);
    }

    private <T> T makeRequest(String method, String path, Object request,
                              Class<T> responseClass, Map<String, String> headers) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);


            if (headers != null) {
                for (var entry : headers.entrySet()) {
                    http.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            String msg = http.getResponseMessage();
            throw new ResponseException(status, "failure: " + msg);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}