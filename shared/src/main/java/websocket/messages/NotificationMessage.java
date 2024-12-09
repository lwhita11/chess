package websocket.messages;

import chess.ChessGame;
import websocket.messages.ServerMessage;

import java.util.Objects;

public class NotificationMessage extends ServerMessage {
    private final String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof websocket.messages.NotificationMessage)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        websocket.messages.NotificationMessage that = (websocket.messages.NotificationMessage) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getMessage());
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "serverMessageType=" + serverMessageType +
                ", message=" + message +
                '}';
    }
}