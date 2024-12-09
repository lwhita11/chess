package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;
import websocket.messages.ServerMessage;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;

    public MakeMoveCommand(String authToken, String gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof websocket.commands.MakeMoveCommand)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        websocket.commands.MakeMoveCommand that = (websocket.commands.MakeMoveCommand) o;
        return Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }

    @Override
    public String toString() {
        return "MakeMoveCommand{" +
                "commandType=" + getCommandType() +
                ", authToken='" + getAuthToken() + '\'' +
                ", gameID='" + getGameID() + '\'' +
                ", move=" + move +
                '}';
    }
}