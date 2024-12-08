package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ChessService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    ChessService service = new ChessService();
    private static final Map<Integer, Session> sessions = new ConcurrentHashMap<>();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String authToken = command.getAuthToken();
            if (service.invalidToken(authToken)) {
                throw new IOException();
            }
            // saveSession(command.getGameID());

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, authToken, command);
                case MAKE_MOVE -> makeMove(session, authToken, command);
                case LEAVE -> leaveGame(session, authToken, command);
                case RESIGN -> resign(session, authToken, command);
            }
        } catch (ResponseException ex){
            System.out.println("Unauthorized");
        }
    }

    private void connect(Session session, String authToken, UserGameCommand command){
        sessions.put(command.getGameID(), session);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        try {
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeMove(Session session, String authToken, UserGameCommand command) {
        ChessGame game = service.getGame(command.getGameID());
        if (game == null) {
            sendError(session, "Game not found.");
            return;
        }

        try {
            service.makeMove(command.getGameID(), command.getAuthToken(), /* additional move details */);
            broadcastToGame(command.getGameID(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
        } catch (InvalidMoveException e) {
            sendError(session, "Invalid move: " + e.getMessage());
        }
    }

    private void leaveGame(Session session, String authToken, UserGameCommand command) {

    }

    private void resign(Session session, String authToken, UserGameCommand command) {

    }

    private void sendError(Session session, String errorMessage) {

    }


}
