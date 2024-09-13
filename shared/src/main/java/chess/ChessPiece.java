package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        switch(board.getPiece(myPosition).pieceType) {
            case KING:
                if ((board.board[myPosition.row+1][myPosition.col] == null) && ((myPosition.row + 1) < 8)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col), null));
                }
                if ((board.board[myPosition.row][myPosition.col+1] == null) && ((myPosition.col + 1) < 8)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col+1), null));
                }

        }

        return moves;
    }
}
