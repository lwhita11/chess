package chess;

import chess.movecalc.MoveCalculator;

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
    private boolean hasMoved = false;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
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

    public void setHasMoved(){
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
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
        MoveCalculator moveCalc = new MoveCalculator();
        switch(board.getPiece(myPosition).pieceType) {
            case KING:
                moves.addAll(moveCalc.kingMoves(board, myPosition));
                break;
            case BISHOP:
                moves.addAll(moveCalc.bishopMoves(board, myPosition));
                break;
            case ROOK:
                moves.addAll(moveCalc.rookMoves(board, myPosition));
                break;
            case KNIGHT:
                moves.addAll(moveCalc.knightMoves(board, myPosition));
                break;
            case QUEEN:
                moves.addAll(moveCalc.bishopMoves(board, myPosition));
                moves.addAll(moveCalc.rookMoves(board, myPosition));
                break;
            case PAWN:
                moves.addAll(moveCalc.pawnMoves(board, myPosition));
                break;
        }
        return moves;
    }


}
