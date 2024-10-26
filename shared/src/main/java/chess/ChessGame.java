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
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
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


    private boolean simulateMove(ChessMove move){

        // To see if a move will put you in Check
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece capturedPiece = board.getPiece(endPos);
        ChessPiece piece = board.getPiece(startPos);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        board.addPiece(endPos, piece);
        board.removePiece(startPos);
        if (isInCheck(teamColor)) {
            board.addPiece(startPos, piece);
            board.addPiece(endPos, capturedPiece);
            return false;
        }
        else {
            board.addPiece(startPos, piece);
            board.addPiece(endPos, capturedPiece);
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

        moves.removeIf(move -> !simulateMove(move));

        //Castling
        if (piece.getPieceType() == ChessPiece.PieceType.KING && !piece.hasMoved()) {
            ChessPiece rook1;
            ChessPiece rook2;
            if (piece.getTeamColor() == TeamColor.WHITE) {
                rook1 = board.getPiece(new ChessPosition(1, 1));
                rook2 = board.getPiece(new ChessPosition(1, 8));
                if (rook1 != null && !rook1.hasMoved() && board.getPiece(new ChessPosition(1, 2)) == null &&
                        board.getPiece(new ChessPosition(1, 3)) == null &&
                        board.getPiece(new ChessPosition(1, 4)) == null){
                    int colChange = -1;
                    ChessMove move = rookMoves(startPosition, colChange);
                    if (move != null) {
                        moves.add(move);
                    }
                }
                if (rook2 != null && !rook2.hasMoved() && board.getPiece(new ChessPosition(1, 7)) == null &&
                        board.getPiece(new ChessPosition(1, 6)) == null){
                    int colChange = 1;
                    ChessMove move = rookMoves(startPosition, colChange);
                    if (move != null) {
                        moves.add(move);
                    }
                }
            }
            if (piece.getTeamColor() == TeamColor.BLACK) {
                rook1 = board.getPiece(new ChessPosition(8, 1));
                rook2 = board.getPiece(new ChessPosition(8, 8));
                if (rook1 != null && !rook1.hasMoved() && board.getPiece(new ChessPosition(8, 2)) == null &&
                        board.getPiece(new ChessPosition(8, 3)) == null &&
                        board.getPiece(new ChessPosition(8, 4)) == null){
                    int colChange = -1;
                    ChessMove move = rookMoves(startPosition, colChange);
                    if (move != null) {
                        moves.add(move);
                    }
                }
                if (rook2 != null && !rook2.hasMoved() && board.getPiece(new ChessPosition(8, 7)) == null &&
                        board.getPiece(new ChessPosition(8, 6)) == null){
                    int colChange = 1;
                    ChessMove move = rookMoves(startPosition, colChange);
                    if (move != null) {
                        moves.add(move);
                    }
                }
            }
        }
        return moves;

    }

    public ChessMove rookMoves(ChessPosition startPosition, int colChange) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        ChessMove move1 = new ChessMove(startPosition,
                new ChessPosition(startPosition.getRow(), startPosition.getColumn() + (colChange)),
                null);
        ChessMove move2 = new ChessMove(startPosition,
                new ChessPosition(startPosition.getRow(), startPosition.getColumn() + (2 * colChange)),
                null);
        if (simulateMove(move1) && simulateMove(move2)) {
            moves.add(move2);
            return move2;
        }
        return null;
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
        //Piece is null
        if (piece == null) {
            throw new InvalidMoveException("Move is invalid");

        }
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        //If move is not in the valid moves
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
        //Castling logic
        if (pieceType == ChessPiece.PieceType.PAWN && (endPos.getRow() == 8 || endPos.getRow() == 1)) {
            board.addPiece(endPos, new ChessPiece(teamColor, move.getPromotionPiece()));
            board.removePiece(startPos);
            whiteTurn = !whiteTurn;
            return;
        }
        if (pieceType == ChessPiece.PieceType.KING && ((startPos.getColumn() - endPos.getColumn() > 1) ||
                (startPos.getColumn() - endPos.getColumn() < -1))) {
            if (startPos.getColumn() - endPos.getColumn() > 1) {
                board.addPiece(new ChessPosition(startPos.getRow(), startPos.getColumn() - 1),
                        new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
                board.removePiece(new ChessPosition(startPos.getRow(), 1));
                board.addPiece(endPos, board.getPiece(startPos));
                board.removePiece(startPos);
                whiteTurn = !whiteTurn;
                piece.setHasMoved();
                return;
            }
            if (startPos.getColumn() - endPos.getColumn() < -1) {
                board.addPiece(new ChessPosition(startPos.getRow(), startPos.getColumn() + 1),
                        new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
                board.removePiece(new ChessPosition(startPos.getRow(), 8));
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
                whiteTurn = !whiteTurn;
                piece.setHasMoved();
                return;
            }
        }
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());
        whiteTurn = !whiteTurn;
        piece.setHasMoved();
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
            if (tempBoard.getPiece(tempPosition).getPieceType() == ChessPiece.PieceType.KING &&
                    tempBoard.getPiece(new ChessPosition(w, j)).getTeamColor() == teamColor) {
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
            ChessPiece testPiece = new ChessPiece(board.getPiece(new ChessPosition(w, j)).getTeamColor(),
                    board.getPiece(testPos).getPieceType());
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
        return cannotMove(teamColor);
    }

    private boolean cannotMove(TeamColor teamColor){
        int possibleMoves = 0;
        for (int i = 0; i < 64; i++) {
            int j = (i % 8) + 1;
            int w = (i / 8) + 1;
            ChessPosition testPos = new ChessPosition(w, j);
            if (board.getPiece(testPos) == null) {
                continue;
            }
            if (board.getPiece(testPos).getTeamColor() != teamColor) {
                continue;
            }
            ChessPiece testPiece = board.getPiece(testPos);
            Collection<ChessMove> testMoves = testPiece.pieceMoves(board, testPos);
            testMoves.removeIf(move -> !simulateMove(move));
            possibleMoves = possibleMoves + testMoves.size();
        }
        if (possibleMoves == 0) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return cannotMove(teamColor);
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
