package server;

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

        // Getters and setters
        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class GamesResponse {
        private List<Map<String, String>> games;

        public List<Map<String, String>> getGames() {
            return games;
        }

        public void setGames(List<Map<String, String>> games) {
            this.games = games;
        }
    }

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
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

    public List<Map<String, String>> listGames(String authToken) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = Map.of("authorization", authToken);
        GamesResponse response = this.makeRequest("GET", path, null, GamesResponse.class, headers);
        return response.getGames();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, Map<String, String> headers) throws ResponseException {
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
            throw new ResponseException(status, "failure: " + status);
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