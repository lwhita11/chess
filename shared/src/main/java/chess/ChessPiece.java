package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPiece {

    public ChessPiece.PieceType pieceType;
    public ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).pieceColor;
        switch(board.getPiece(myPosition).pieceType) {
            case KING:
                if ((myPosition.row != 7) && (board.board[myPosition.row+1][myPosition.col] == null || board.board[myPosition.row+1][myPosition.col].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col), null));
                }
                if ((myPosition.col != 7) && (board.board[myPosition.row][myPosition.col+1] == null || board.board[myPosition.row][myPosition.col+1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col+1), null));
                }
                if ( (myPosition.row != 0) && (board.board[myPosition.row-1][myPosition.col] == null || board.board[myPosition.row-1][myPosition.col].pieceColor != myColor) ) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col), null));
                }
                if ((myPosition.col != 0) && (board.board[myPosition.row][myPosition.col-1] == null || board.board[myPosition.row][myPosition.col-1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col-1), null));
                }
                if ((myPosition.row  !=  7) && (myPosition.col != 7) && (board.board[myPosition.row+1][myPosition.col+1] == null || board.board[myPosition.row+1][myPosition.col+1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col+1), null));
                }
                if ((myPosition.row  !=  0) && (myPosition.col != 0) && (board.board[myPosition.row-1][myPosition.col-1] == null || board.board[myPosition.row-1][myPosition.col-1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col-1), null));
                }
                if ((myPosition.row  !=  7) && (myPosition.col != 0) && (board.board[myPosition.row+1][myPosition.col-1] == null || board.board[myPosition.row+1][myPosition.col-1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col-1), null));
                }
                if ((myPosition.row  !=  0) && (myPosition.col != 7) && (board.board[myPosition.row-1][myPosition.col+1] == null || board.board[myPosition.row-1][myPosition.col+1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col+1), null));
                }
        }
        return moves;
    }
}
