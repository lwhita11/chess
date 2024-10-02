package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return whiteTurn == chessGame.whiteTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whiteTurn, board);
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


    private boolean SimulateMove(ChessMove move){
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPos);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        board.addPiece(endPos, piece);
        board.removePiece(startPos);
        if (isInCheck(teamColor)) {
            board.addPiece(startPos, piece);
            board.removePiece(endPos);
            return false;
        }
        else {
            board.addPiece(startPos, piece);
            board.removePiece(endPos);
            return true;
        }
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
        ChessGame.TeamColor teamColor = board.getPiece(startPosition).getTeamColor();
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        //Simulate Move to see if it is in check

        moves.removeIf(move -> !SimulateMove(move));

        return moves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPos);
        if (piece == null) {
            throw new InvalidMoveException("Move is invalid");

        }
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();
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
        if (pieceType == ChessPiece.PieceType.PAWN && (endPos.getRow() == 8 || endPos.getRow() == 1)) {
            board.addPiece(endPos, new ChessPiece(teamColor, move.getPromotionPiece()));
            board.removePiece(startPos);
            whiteTurn = !whiteTurn;
            return;
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

        //Check King safety setup
        ChessBoard tempBoard = board;

        ChessPosition kingLocation = new ChessPosition(0, 0);

        // Find King
        for (int i = 0; i < 64; i++) {
            int j = (i % 8) + 1;
            int w = (i / 8) + 1;
            ChessPosition tempPosition = new ChessPosition(w, j);
            if (tempBoard.getPiece(tempPosition) == null) {
                continue;
            }
            if (tempBoard.getPiece(tempPosition).getPieceType() == ChessPiece.PieceType.KING && tempBoard.getPiece(new ChessPosition(w, j)).getTeamColor() == teamColor) {
                kingLocation = new ChessPosition(w, j);
            }
        }

        //Check King Safety
        for (int i = 0; i < 64; i++) {
            int j = (i % 8) + 1;
            int w = (i / 8) + 1;
            ChessPosition testPos = new ChessPosition(w, j);
            if (tempBoard.getPiece(testPos) == null) {
                continue;
            }
            if (tempBoard.getPiece(testPos).getTeamColor() == teamColor) {
                continue;
            }
            ChessPiece testPiece = new ChessPiece(board.getPiece(new ChessPosition(w, j)).getTeamColor(), board.getPiece(testPos).getPieceType());
            Collection<ChessMove> testMoves = testPiece.pieceMoves(board, testPos);
            ChessMove invalidMove = new ChessMove(testPos, kingLocation, null);
            ChessMove invalidMove2 = new ChessMove(testPos, kingLocation, ChessPiece.PieceType.QUEEN);
            ChessMove invalidMove3 = new ChessMove(testPos, kingLocation, ChessPiece.PieceType.ROOK);
            ChessMove invalidMove4 = new ChessMove(testPos, kingLocation, ChessPiece.PieceType.BISHOP);
            ChessMove invalidMove5 = new ChessMove(testPos, kingLocation, ChessPiece.PieceType.KNIGHT);
            if (testMoves.contains(invalidMove)) {
                return true;
            }
            if (testMoves.contains(invalidMove2)) {
                return true;
            }
            if (testMoves.contains(invalidMove3)) {
                return true;
            }
            if (testMoves.contains(invalidMove4)) {
                return true;
            }
            if (testMoves.contains(invalidMove5)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
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
