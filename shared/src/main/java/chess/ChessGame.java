package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private boolean whiteTurn = true;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (!whiteTurn) {
            return TeamColor.BLACK;
        }
        else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.BLACK) {
            whiteTurn = false;
        }
        else if (team == TeamColor.WHITE) {
            whiteTurn = true;
        }
        else {
            throw new RuntimeException("invalid team's turn");
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Move is invalid");

        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Move is invalid");
        }
        //Black piece moves on White turn
        if (whiteTurn && board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK) {
            throw new InvalidMoveException("Move is invalid");
        }
        //White piece moves on Black turn
        else if (!whiteTurn && board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE) {
            throw new InvalidMoveException("Move is invalid");
        }

        //Check King safety setup
        ChessBoard tempBoard = board;
        ChessGame.TeamColor tempColor;
        boolean tempTurn = whiteTurn;
        if (whiteTurn) {
            tempColor = TeamColor.WHITE;
        }
        else {
            tempColor = TeamColor.WHITE;
        }

        ChessPosition kingLocation = new ChessPosition(0, 0);
        tempBoard.addPiece(move.getEndPosition(), tempBoard.getPiece(move.getStartPosition()));
        tempBoard.removePiece(move.getStartPosition());

        //Check King safety
        for (int i = 0; i < 64; i++) {
            int j = i % 8;
            int w = i / 8;
            if (tempBoard.getPiece(new ChessPosition(w, j)) == null) {
                continue;
            }
            else if (tempBoard.getPiece(new ChessPosition(w, j)).getPieceType() == ChessPiece.PieceType.KING && tempBoard.getPiece(new ChessPosition(w, j)).getTeamColor() == tempColor) {
                kingLocation = new ChessPosition(w, j);
            }
            Collection<ChessMove> testMoves = validMoves(new ChessPosition(w, j));
            ChessMove invalidMove = new ChessMove(new ChessPosition(w, j), kingLocation, null);
            ChessMove invalidMove2 = new ChessMove(new ChessPosition(w, j), kingLocation, ChessPiece.PieceType.QUEEN);
            ChessMove invalidMove3 = new ChessMove(new ChessPosition(w, j), kingLocation, ChessPiece.PieceType.ROOK);
            ChessMove invalidMove4 = new ChessMove(new ChessPosition(w, j), kingLocation, ChessPiece.PieceType.BISHOP);
            ChessMove invalidMove5 = new ChessMove(new ChessPosition(w, j), kingLocation, ChessPiece.PieceType.KNIGHT);
            if (testMoves.contains(invalidMove)) {
                throw new InvalidMoveException("Move is invalid");
            }
            if (testMoves.contains(invalidMove2)) {
                throw new InvalidMoveException("Move is invalid");
            }
            if (testMoves.contains(invalidMove3)) {
                throw new InvalidMoveException("Move is invalid");
            }
            if (testMoves.contains(invalidMove4)) {
                throw new InvalidMoveException("Move is invalid");
            }
            if (testMoves.contains(invalidMove5)) {
                throw new InvalidMoveException("Move is invalid");
            }
        }

        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());
        whiteTurn = !whiteTurn;
        //TODO
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
