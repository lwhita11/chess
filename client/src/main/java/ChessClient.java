import java.util.Arrays;

import exception.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;
import ui.EscapeSequences.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;


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
                // case "list" -> listGames();
                // case "signout" -> signOut();
                case "quit" -> "quit";
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
            server.registerUser(username, password, email);
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
            server.login(username, password);
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    // public String logout()

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


