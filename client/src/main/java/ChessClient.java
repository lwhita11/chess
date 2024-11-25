import java.util.*;

import chess.ChessGame;
import exception.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;
import ui.EscapeSequences.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private ChessGame.TeamColor teamColor;
    private List<Map<String, Object>> gameList = new ArrayList<>();


    public ChessClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "list" -> listGames();
                case "logout" -> logout();
                case "quit" -> "quit";
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (state != State.SIGNEDOUT){
            throw new ResponseException(400, "Invalid command");
        }
        if (params.length == 3) {
            username = params[0];
            String password = params[1];
            String email = params[2];
            authToken = server.registerUser(username, password, email).getAuthToken();
            state = State.SIGNEDIN;
            return String.format("Successfully registered %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (state != State.SIGNEDOUT){
            throw new ResponseException(400, "Invalid command");
        }
        if (params.length == 2) {
            username = params[0];
            String password = params[1];
            authToken = server.login(username, password).getAuthToken();
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String logout() throws ResponseException {
        if (state != State.SIGNEDOUT){
            throw new ResponseException(400, "Invalid command");
        }
        server.logout(authToken);
        state = State.SIGNEDOUT;
        return ("You successfully logged out");
    }

    public String createGame(String... params) throws ResponseException {
        if (state != State.SIGNEDIN) {
            throw new ResponseException(400, "Invalid command");
        }
        if (params.length == 1) {
            String gameName = params[0];
            String gameID = server.createGame(gameName, authToken);
            return String.format("You created game: %s with ID: %s", gameName, gameID);
        }
        throw new ResponseException(400, "Expected: <GAMENAME>");
    }

    public String listGames() throws ResponseException {
        if (state != State.SIGNEDIN) {
            throw new ResponseException(400, "Invalid command");
        }
        List<Map<String, Object>> games = server.listGames(authToken);
        String response = String.format("%-10s %-5s%n\n", "GameName", "GameNumber");
        for (int i = 0; i < games.size(); i++) {
            String gameNum = String.valueOf(i + 1);
            String gameID = (String) games.get(i).get("gameID");
            Map<String, Object> thisGame = new HashMap<>();
            thisGame.put(gameNum, gameID);
            gameList.add(thisGame);
            response = String.format(response + "%-10s %-5s%n", games.get(i).get("gameName"),
                    gameNum);
        }
        return response;
    }

    public String joinGame(String... params) throws ResponseException {
        if (state != State.SIGNEDIN) {
            throw new ResponseException(400, "Invalid command");
        }
        if (params.length == 2) {
            String gameNumber = params[0];

            if (params[1].equalsIgnoreCase("BLACK")) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else if (params[1].equalsIgnoreCase("WHITE")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else {
                throw new ResponseException(400, "Invalid team color. Expected: [BLACK|WHITE]");
            }

            if (gameList == null || gameList.isEmpty()) {
                throw new ResponseException(400, "No games available to join. Please list games first.");
            }
            int gameIndex;
            try {
                gameIndex = Integer.parseInt(gameNumber) - 1;
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Invalid game number format.");
            }
            if (gameIndex < 0 || gameIndex >= gameList.size()) {
                throw new ResponseException(400, "Game number out of range.");
            }

            Map<String, Object> gameMap = gameList.get(gameIndex);
            String gameID = (String) gameMap.get(gameNumber);
            if (gameID == null) {
                throw new ResponseException(500, "Game ID not found for the selected game.");
            }

            System.out.println(gameID);
            ChessGame game = server.joinGame(gameID, teamColor, authToken);
            System.out.println(game.getBoard().toString());
            return String.format("You joined game %s ", gameNumber);
        }
        throw new ResponseException(400, "Expected: <GAMENUMBER> [BLACK|WHITE]");
    }

    public String help() {
        String description = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
        String option = EscapeSequences.SET_TEXT_COLOR_BLUE;
        if (state == State.SIGNEDOUT) {
            return option + "register <USERNAME> <PASSWORD> <EMAIL>" + description + " - to create an account\n" +
                    option + "login <USERNAME> <PASSWORD>" + description + " - to login to an existing account\n" +
                    option + "quit" + description + " - quit playing chess\n" +
                    option + "help" + description + " - list commands";
        }
        else if (state == State.SIGNEDIN) {
            return option + "logout" + description + " - to logout of your account\n" +
                    option + "create <GAMENAME>" + description + " - create a new Chess game\n" +
                    option + "listgames" + description + " - lists all chess games\n" +
                    option + "join <GAMENUMBER> [WHITE|BLACK]" + description + " - join a Chess Game as White or Black\n" +
                    option + "observe <GAMENUMBER>" + description + " - lists all chess games\n" +
                    option + "help" + description + " - list commands";
        }
        else {
            return "TODO (other states)";
        }
    }
}


