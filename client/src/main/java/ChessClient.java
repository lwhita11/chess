import java.util.*;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
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
        List<Map<String, Object>> thisList = new ArrayList<>();
        List<Map<String, Object>> games = server.listGames(authToken);
        String response = String.format("%-10s %-5s%n\n", "GameName", "GameNumber");
        for (int i = 0; i < games.size(); i++) {
            String gameNum = String.valueOf(i + 1);
            String gameID = (String) games.get(i).get("gameID");
            Map<String, Object> thisGame = new HashMap<>();
            thisGame.put(gameNum, gameID);
            thisList.add(thisGame);
            response = String.format(response + "%-10s %-5s%n", games.get(i).get("gameName"),
                    gameNum);
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
            if (teamColor == ChessGame.TeamColor.BLACK) {
                return boardToStringBlack(game.getBoard());
            }
            else {
                return boardToStringWhite(game.getBoard());
            }
        }
        throw new ResponseException(400, "Expected: <GAMENUMBER> [BLACK|WHITE]");
    }

    private String boardToStringBlack(ChessBoard board) {
        String row = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "\t h\t g\t f\t e\t d\t c\t b\t a\t\t " + EscapeSequences.RESET_BG_COLOR + "\n";
        List<String> rows = new ArrayList<>();
        String row1 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "1\t";
        String row2 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "2\t";
        String row3 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "3\t";
        String row4 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "4\t";
        String row5 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "5\t";
        String row6 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "6\t";
        String row7 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "7\t";
        String row8 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "8\t";

        String result = row;

        rows.add(row1);rows.add(row2);rows.add(row3);rows.add(row4);rows.add(row5);rows.add(row6);rows.add(row7);
        rows.add(row8);
        result = iterateBoard(0, 0, 1,1, board, rows, result);
        return result + row + EscapeSequences.RESET_BG_COLOR;
    }

    private String iterateBoard(int iVal, int jVal, int iIter, int jIter, ChessBoard board, List<String> rows, String result) {
        for (int i = iVal; (iVal > 0 ? i >= 0 : i < 8); i += iIter) {
            for (int j = jVal; j < 8; j += jIter) {
                String background;
                ChessPosition currPos = new ChessPosition(i + 1, j + 1);
                ChessPiece currPiece = board.getPiece(currPos);
                if ((i + j + iVal - 1) % 2 == 0) {
                    background = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
                } else {
                    background = EscapeSequences.SET_BG_COLOR_WHITE;
                }
                if (currPiece == null) {
                    rows.set(i, rows.get(i) + background + "\t");
                    continue;
                }
                ChessGame.TeamColor pieceColor = currPiece.getTeamColor();
                if (pieceColor == ChessGame.TeamColor.BLACK) {
                    switch (currPiece.getPieceType()) {
                        case ChessPiece.PieceType.PAWN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_PAWN + "\t");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_ROOK + "\t");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_KNIGHT + "\t");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_BISHOP + "\t");
                            break;
                        case ChessPiece.PieceType.KING:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_KING + "\t");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_QUEEN + "\t");
                            break;
                    }
                }

                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    switch (currPiece.getPieceType()) {
                        case ChessPiece.PieceType.PAWN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_PAWN + "\t");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_ROOK + "\t");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_KNIGHT + "\t");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_BISHOP + "\t");
                            break;
                        case ChessPiece.PieceType.KING:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_KING + "\t");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_QUEEN + "\t");
                            break;
                    }
                }
            }
            result = result + rows.get(i) + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "\t" + Integer.toString(i + 1)
                    + EscapeSequences.RESET_BG_COLOR + "\n";
        }
        return result;
    }

    private String boardToStringWhite(ChessBoard board) {
        String row = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "\t a\t b\t c\t d\t e\t f\t g\t h\t\t " + EscapeSequences.RESET_BG_COLOR + "\n";
        List<String> rows = new ArrayList<>();
        String row1 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "8\t";
        String row2 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "7\t";
        String row3 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "6\t";
        String row4 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "5\t";
        String row5 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "4\t";
        String row6 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "3\t";
        String row7 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "2\t";
        String row8 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "1\t";

        String result = row;

        rows.add(row1);rows.add(row2);rows.add(row3);rows.add(row4);rows.add(row5);rows.add(row6);rows.add(row7);
        rows.add(row8);
        result = iterateBoard(7, 0, -1,1, board, rows, result);
        return result + row + EscapeSequences.RESET_BG_COLOR;
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
        else {
            return "TODO (other states)";
        }
    }
}


