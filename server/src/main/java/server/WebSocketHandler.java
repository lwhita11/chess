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
    private static final Map<String, List<Session>> sessions = new ConcurrentHashMap<>();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthToken();
        ChessMove move = command.getMove();
//        if (service.invalidToken(authToken)) {
//            throw new IOException();
//        }
        // saveSession(command.getGameID());

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, authToken, command);
            case MAKE_MOVE -> makeMove(session, authToken, command, move);
            case LEAVE -> leaveGame(session, authToken, command);
            case RESIGN -> resign(session, authToken, command);
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
    }

//    @OnWebSocketClose
//    public void onClose(Session session) {
//        System.out.println("WebSocket closed: " + session.getRemoteAddress());
//    }

    private void connect(Session session, String authToken, UserGameCommand command) throws ResponseException {
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

    private void makeMove(Session session, String authToken, UserGameCommand command, ChessMove chessMove) {

        String gameID = command.getGameID();
        if (service.invalidID(gameID)) {
            sendError(session, new ErrorMessage("Error: Invalid game number"));
            return;
        }
        if (service.invalidToken(authToken)) {
            sendError(session, new ErrorMessage("Error: Invalid authToken"));
            return;
        }
        if (chessMove == null) {
            sendError(session, new ErrorMessage("Error: Invalid Chess Move"));
        }

        ChessGame game = service.getGame(command.getGameID());

        Collection<ChessMove> validMoves = game.validMoves(chessMove.getStartPosition());
        if (!validMoves.contains(chessMove)) {
            sendError(session, new ErrorMessage("Error: Invalid Chess Move"));
        }

        ChessBoard board = service.makeMove(command.getGameID(), command.getAuthToken(), chessMove);

        broadcastToGame(command.getGameID(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION), session);
    }

    private void leaveGame(Session session, String authToken, UserGameCommand command) {

    }

    private void resign(Session session, String authToken, UserGameCommand command) {

    }

//    @OnWebSocketError
//    public void onError(Session session) {
//        System.out.println("WebSocket error: " + session.getRemoteAddress());
//    }

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
