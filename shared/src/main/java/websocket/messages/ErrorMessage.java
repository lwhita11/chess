package websocket.messages;

import chess.ChessGame;
import websocket.messages.ServerMessage;

import java.util.Objects;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
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
        websocket.messages.ErrorMessage that = (websocket.messages.ErrorMessage) o;
        return Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getMessage());
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "serverMessageType=" + serverMessageType +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
