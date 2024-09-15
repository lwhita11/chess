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

    @Override
    public String toString() {
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            if (pieceType == ChessPiece.PieceType.KING) {
                return "K";
            }
            else if (pieceType == ChessPiece.PieceType.QUEEN) {
                return "Q";
            }
            else if (pieceType == ChessPiece.PieceType.BISHOP) {
                return "B";
            }
            else if (pieceType == ChessPiece.PieceType.KNIGHT) {
                return "N";
            }
            else if (pieceType == ChessPiece.PieceType.ROOK) {
                return "R";
            }
            else if (pieceType == ChessPiece.PieceType.PAWN) {
                return "P";
            }
            else {
                throw new IllegalArgumentException("Invalid piece type: " + pieceType);
            }
        }
        else if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            if (pieceType == ChessPiece.PieceType.KING) {
                return "k";
            }
            else if (pieceType == ChessPiece.PieceType.QUEEN) {
                return "q";
            }
            else if (pieceType == ChessPiece.PieceType.BISHOP) {
                return "b";
            }
            else if (pieceType == ChessPiece.PieceType.KNIGHT) {
                return "n";
            }
            else if (pieceType == ChessPiece.PieceType.ROOK) {
                return "r";
            }
            else if (pieceType == ChessPiece.PieceType.PAWN) {
                return "p";
            }
            else {
                throw new IllegalArgumentException("Invalid piece type: " + pieceType);
            }
        }
        else {
            throw new IllegalArgumentException("Invalid piece color: " + pieceColor);
        }
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
                if ((myPosition.row != 8) && (board.board[myPosition.row+1][myPosition.col] == null || board.board[myPosition.row+1][myPosition.col].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col), null));
                }
                if ((myPosition.col != 8) && (board.board[myPosition.row][myPosition.col+1] == null || board.board[myPosition.row][myPosition.col+1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col+1), null));
                }
                if ( (myPosition.row != 1) && (board.board[myPosition.row-1][myPosition.col] == null || board.board[myPosition.row-1][myPosition.col].pieceColor != myColor) ) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col), null));
                }
                if ((myPosition.col != 1) && (board.board[myPosition.row][myPosition.col-1] == null || board.board[myPosition.row][myPosition.col-1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col-1), null));
                }
                if ((myPosition.row  !=  8) && (myPosition.col != 8) && (board.board[myPosition.row+1][myPosition.col+1] == null || board.board[myPosition.row+1][myPosition.col+1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col+1), null));
                }
                if ((myPosition.row  !=  1) && (myPosition.col != 1) && (board.board[myPosition.row-1][myPosition.col-1] == null || board.board[myPosition.row-1][myPosition.col-1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col-1), null));
                }
                if ((myPosition.row  !=  8) && (myPosition.col != 1) && (board.board[myPosition.row+1][myPosition.col-1] == null || board.board[myPosition.row+1][myPosition.col-1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col-1), null));
                }
                if ((myPosition.row  !=  1) && (myPosition.col != 8) && (board.board[myPosition.row-1][myPosition.col+1] == null || board.board[myPosition.row-1][myPosition.col+1].pieceColor != myColor)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col+1), null));
                }
            case BISHOP:
                int i = 0;
                int j = 0;
                boolean enemyPiece = false;
                do {
                    if (enemyPiece) {
                        break;
                    }
                    if (board.board[myPosition.row + i][myPosition.col + j] != null && board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                        enemyPiece = true;
                    }
                    if (i != 0) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row + i, myPosition.col + j), null));
                    }
                    i++;
                    j++;
                }
                while ((myPosition.row + i < 9 && myPosition.col + j < 9) && (board.board[myPosition.row + i][myPosition.col + j] == null || (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor && !enemyPiece)));
                i = 0;
                j = 0;
                enemyPiece = false;
                do {
                    if (enemyPiece) {
                        break;
                    }
                    if (i != 0) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row + i, myPosition.col + j), null));
                    }
                    if (board.board[myPosition.row + i][myPosition.col + j] != null && board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                        enemyPiece = true;
                    }
                    i++;
                    j--;
                }
                while ((myPosition.row + i < 9 && myPosition.col + j > 0) && (board.board[myPosition.row + i][myPosition.col + j] == null || (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor && !enemyPiece)));
                i = 0;
                j = 0;
                enemyPiece = false;
                do {
                    if (enemyPiece) {
                        break;
                    }
                    if (i != 0) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row + i, myPosition.col + j), null));
                    }
                    if (board.board[myPosition.row + i][myPosition.col + j] != null && board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                        enemyPiece = true;
                    }
                    i--;
                    j++;
                }
                while ((myPosition.row + i > 0 && myPosition.col + j < 9) && (board.board[myPosition.row + i][myPosition.col + j] == null || (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor && !enemyPiece)));
                i = 0;
                j = 0;
                enemyPiece = false;
                do {
                    if (enemyPiece) {
                        break;
                    }
                    if (i != 0) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row + i, myPosition.col + j), null));
                    }
                    if (board.board[myPosition.row + i][myPosition.col + j] != null && board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                        enemyPiece = true;
                    }
                    i--;
                    j--;
                }
                while ((myPosition.row + i > 0 && myPosition.col + j > 0) && (board.board[myPosition.row + i][myPosition.col + j] == null || (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor && !enemyPiece)));
        }
        return moves;
    }
}
