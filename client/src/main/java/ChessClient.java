import java.util.*;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;
import ui.EscapeSequences.*;
import ui.PrintBoard;

public class ChessClient {
    private boolean isResigned = false;
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private ChessGame.TeamColor teamColor;
    private ChessGame chessGame;
    private ChessBoard chessBoard;
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
                case "observe" -> observeGame(params);
                case "redraw" -> redraw();
//                case "leave" -> leave();
//                case "makemove" -> makeMove(params);
//                case "resign" -> resign();
//                case "y" -> confirmResign();
//                case "n" -> declineResign();
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
            try {
                authToken = server.registerUser(username, password, email).getAuthToken();
            } catch (ResponseException e) {
                throw new ResponseException(400, "Could not register user. Try a different username");
            }
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
        if (state != State.SIGNEDIN){
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
        List<Map<String, Object>> thisList = new ArrayList<>();
        List<Map<String, Object>> games = server.listGames(authToken);
        String response = String.format("%-15s %-15s %-15s %-15s%n\n", "GameName", "GameNumber",
                "WhitePlayer", "BlackPlayer");
        for (int i = 0; i < games.size(); i++) {
            String gameNum = String.valueOf(i + 1);
            String gameID = (String) games.get(i).get("gameID");
            String blackUsername = (String) games.get(i).get("blackUsername");
            String whiteUsername = (String) games.get(i).get("whiteUsername");
            if (blackUsername == null) {
                blackUsername = "NONE";
            }
            if (whiteUsername == null) {
                whiteUsername = "NONE";
            }
            Map<String, Object> thisGame = new HashMap<>();

            thisGame.put(gameNum, gameID);
            thisList.add(thisGame);
            response = String.format(response + "%-15s %-15s %-15s %-15s%n", games.get(i).get("gameName"),
                    gameNum, whiteUsername, blackUsername);
        }
        gameList = thisList;
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
                throw new ResponseException(400, "Game not available to join. List available games");
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
            if (gameMap == null) {
                throw new ResponseException(500, "Game number not found for the selected game. List available " +
                        "games or create a new game");
            }
            String gameID = (String) gameMap.get(gameNumber);
            if (gameID == null) {
                throw new ResponseException(500, "Game ID not found for the selected game.");
            }
            ChessGame game = server.joinGame(gameID, teamColor, authToken);
            System.out.println(game.getBoard().toString());
            state = State.JOINEDGAME;
            chessGame = game;
            chessBoard = game.getBoard();
            if (teamColor == ChessGame.TeamColor.BLACK) {
                return PrintBoard.boardToStringBlack(game.getBoard());
            }
            else {
                return PrintBoard.boardToStringWhite(game.getBoard());
            }
        }
        throw new ResponseException(400, "Expected: <GAMENUMBER> [BLACK|WHITE]");
    }

    public String observeGame(String... params) throws ResponseException {
        if (state != State.SIGNEDIN){
            throw new ResponseException(400, "Invalid command");
        }
        ChessGame thisGame;
        if (params.length == 1) {
            String gameNumber = params[0];
            if (gameList == null || gameList.isEmpty()) {
                throw new ResponseException(400, "No games available to join. Please list games or create game first.");
            }
            int gameIndex;
            try {
                gameIndex = Integer.parseInt(gameNumber) - 1;
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Invalid game number format.");
            }
            if (gameIndex > gameList.size() - 1) {
                throw new ResponseException(500, "Game number not found for the selected game. List available " +
                        "games or create a new game");
            }
            Map<String, Object> gameMap = gameList.get(gameIndex);
            if (gameMap == null) {
                throw new ResponseException(500, "Game number not found for the selected game. List available " +
                        "games or create a new game");
            }
            String gameID = (String) gameMap.get(gameNumber);
            if (gameID == null) {
                throw new ResponseException(500, "Game ID not found for the selected game.");
            }
            thisGame = server.observeGame(gameID, authToken);
            ChessBoard thisBoard = thisGame.getBoard();
            chessBoard = thisBoard;
            chessGame = thisGame;
            return PrintBoard.boardToStringWhite(thisBoard);
        }
        throw new ResponseException(400, "Expected: observe <GAMENUMBER>");
    }

    public String redraw() throws ResponseException {
        if (state != State.JOINEDGAME) {
            throw new ResponseException(400, "Invalid command");
        }
        return PrintBoard.boardToStringWhite(chessBoard);
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
                    option + "join <GAMENUMBER> [WHITE|BLACK]" + description + " - join a Chess Game as White " +
                    "or Black\n" +
                    option + "observe <GAMENUMBER>" + description + " - lists all chess games\n" +
                    option + "help" + description + " - list commands";
        }
        else if (state == State.JOINEDGAME) {
            return option + "redraw" + description + " - redraw board\n" +
                    option + "leave" + description + " - leave current game\n" +
                    option + "makemove <POSITION(a-h)(1-8)> <POSITION(a-h)(1-8)>" + description +
                    " - make Chess Move\n" +
                    option + "resign" + description + " - resign the game" +
                    option + "showmoves" + description + " - highlights legal moves\n" +
                    option + "help" + description + " - list commands";
        }
        else {
            return "unexpected state";
        }
    }
}


