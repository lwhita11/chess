package server;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.ChessService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    ChessService service = new ChessService();
    Map<String, List<Session>> sessions = new ConcurrentHashMap<>();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand moveCommand = null;
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
            System.out.println("updated moveCommand");
            ChessMove move = moveCommand.getMove();
            System.out.println("chessMove: " + move.toString());
        }
        String authToken = command.getAuthToken();
        System.out.println("processing this command: " + message);

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, authToken, command);
            case MAKE_MOVE -> makeMove(session, authToken, moveCommand);
            case LEAVE -> leaveGame(session, authToken, command);
            case RESIGN -> resign(session, authToken, command);
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
    }


    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed: " + session.getRemoteAddress());

        sessions.forEach((gameID, sessionList) -> {
            synchronized (sessionList) {
                if (sessionList.remove(session)) {
                    System.out.println("Removed session from gameID: " + gameID);
                    if (sessionList.isEmpty()) {
                        sessions.remove(gameID);
                        System.out.println("Removed gameID: " + gameID + " as it no longer has any sessions.");
                    }
                }
            }
        });
    }

    private void connect(Session session, String authToken, UserGameCommand command) {
        System.out.println("Connected: " + session.getRemoteAddress().getAddress());
        List<Session> listSession = sessions.get(command.getGameID());
        if (listSession == null) {
            listSession = new ArrayList<>();
        }
        listSession.add(session);
        sessions.put(command.getGameID(), listSession);
        String gameID = command.getGameID();
        if (service.invalidID(gameID)) {
            ServerMessage message = new ErrorMessage("Error: Invalid game number");
            try {
                session.getRemote().sendString(new Gson().toJson(message));
                System.out.println("errorMessage: " + new Gson().toJson(message));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (service.invalidToken(authToken)) {
            ServerMessage message = new ErrorMessage("Error: Invalid authToken");
            try {
                session.getRemote().sendString(new Gson().toJson(message));
                System.out.println("errorMessage: " + new Gson().toJson(message));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ChessGame game = service.getGame(gameID);
        ServerMessage message = new LoadGameMessage(game);
        ServerMessage broadcastMessage = new NotificationMessage("UPDATE joined the game");
        try {
            System.out.println("sending message: " + new Gson().toJson(message));
            session.getRemote().sendString(new Gson().toJson(message));
            broadcastToGame(command.getGameID(), broadcastMessage, session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeMove(Session session, String authToken, MakeMoveCommand command) {

        ChessMove chessMove = command.getMove();
        System.out.println("in makeMove");
        if (chessMove == null) {
            sendError(session, new ErrorMessage("Error: Invalid Chess Move"));
            return;
        }
        System.out.println("move: " + chessMove.toString());
        String gameID = command.getGameID();
        if (service.invalidID(gameID)) {
            sendError(session, new ErrorMessage("Error: Invalid game number"));
            return;
        }
        if (service.invalidToken(authToken)) {
            sendError(session, new ErrorMessage("Error: Invalid authToken"));
            return;
        }

        ChessGame game = service.getGame(command.getGameID());
        if (game.getGameOver()) {
            sendError(session, new ErrorMessage("Error: Game is over"));
        }

        Collection<ChessMove> validMoves = game.validMoves(chessMove.getStartPosition());
        if (!validMoves.contains(chessMove)) {
            sendError(session, new ErrorMessage("Error: Invalid Chess Move"));
            return;
        }

        ChessGame.TeamColor turnColor = game.getTeamTurn();
        String username = service.getUsername(authToken);
        Map<String, Object> gameMap = service.getGameMap(gameID);
        String blackUsername = (String) gameMap.get("blackUsername");
        String whiteUsername = (String) gameMap.get("whiteUsername");
        System.out.println("User attempting to move: " + username);
        System.out.println("WhiteUsername: " + whiteUsername);
        System.out.println("BlackUsername: " + blackUsername);
        System.out.println("Turn color: " + turnColor.toString());
        if (turnColor == ChessGame.TeamColor.BLACK && !username.equals(blackUsername)) {
            sendError(session, new ErrorMessage("Error: Cannot move when not your turn"));
            return;
        }
        if (turnColor == ChessGame.TeamColor.WHITE && !username.equals(whiteUsername)) {
            sendError(session, new ErrorMessage("Error: Cannot move when not your turn"));
            return;
        }

        // service.makeMove(gameID, authToken, chessMove);
        System.out.println("preparing to broadcast move message");

        ServerMessage broadcastMessage = new NotificationMessage("UPDATE made move: " + chessMove.toString());
        broadcastToGame(command.getGameID(), broadcastMessage, session);
        System.out.println("broadcasted move message");

        ServerMessage message = new LoadGameMessage(game);
        service.makeMove(gameID, authToken, chessMove);
        try {
            System.out.println("sending message: " + new Gson().toJson(message));
            session.getRemote().sendString(new Gson().toJson(message));
            broadcastToGame(command.getGameID(), message, session);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void leaveGame(Session session, String authToken, UserGameCommand command) {

    }

    private void resign(Session session, String authToken, UserGameCommand command) {

    }


    private void sendError(Session session, ServerMessage message) {
        try {
            session.getRemote().sendString(new Gson().toJson(message));
            System.out.println("errorMessage: " + new Gson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastToGame(String gameID, ServerMessage message, Session currSession) {
        if (sessions.containsKey(gameID)) {
            try {
                List<Session> sessionList = sessions.get(gameID);
                System.out.println("Broadcasted message: " + message);
                for (int i = 0; i < sessionList.size(); i++) {
                    if (sessionList.get(i) == currSession) {
                        continue;
                    }
                    System.out.println("Sending Broadcasted message to index: " + Integer.toString(i));
                    sessionList.get(i).getRemote().sendString(new Gson().toJson(message));
                    System.out.println("Sent broadcasted message");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
