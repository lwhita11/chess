package chess.MoveCalc;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if ((myPosition.row != 8) && (board.board[myPosition.row+1][myPosition.col] == null
                || board.board[myPosition.row+1][myPosition.col].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col),
                    null));
        }
        if ((myPosition.col != 8) && (board.board[myPosition.row][myPosition.col+1] == null
                || board.board[myPosition.row][myPosition.col+1].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col+1),
                    null));
        }
        if ( (myPosition.row != 1) && (board.board[myPosition.row-1][myPosition.col] == null
                || board.board[myPosition.row-1][myPosition.col].pieceColor != myColor) ) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col),
                    null));
        }
        if ((myPosition.col != 1) && (board.board[myPosition.row][myPosition.col-1] == null
                || board.board[myPosition.row][myPosition.col-1].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row, myPosition.col-1),
                    null));
        }
        if ((myPosition.row  !=  8) && (myPosition.col != 8) &&
                (board.board[myPosition.row+1][myPosition.col+1] == null ||
                        board.board[myPosition.row+1][myPosition.col+1].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col+1),
                    null));
        }
        if ((myPosition.row  !=  1) && (myPosition.col != 1) &&
                (board.board[myPosition.row-1][myPosition.col-1] == null ||
                        board.board[myPosition.row-1][myPosition.col-1].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col-1),
                    null));
        }
        if ((myPosition.row  !=  8) && (myPosition.col != 1) &&
                (board.board[myPosition.row+1][myPosition.col-1] == null ||
                        board.board[myPosition.row+1][myPosition.col-1].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row+1, myPosition.col-1),
                    null));
        }
        if ((myPosition.row  !=  1) && (myPosition.col != 8) &&
                (board.board[myPosition.row-1][myPosition.col+1] == null ||
                        board.board[myPosition.row-1][myPosition.col+1].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row-1, myPosition.col+1),
                    null));
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int stem = 2;
        int branch = 1;
        int row = myPosition.row;
        int col = myPosition.col;
        if (row + stem < 9 && col + branch < 9 && (board.board[row + stem][col + branch] == null ||
                board.board[row + stem][col + branch].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row+stem, col+branch),
                    null));
        }
        if (row + stem < 9 && col - branch > 0 && (board.board[row + stem][col - branch] == null ||
                board.board[row + stem][col - branch].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row+stem, col-branch),
                    null));
        }
        if (row - stem > 0 && col + branch < 9 && (board.board[row - stem][col + branch] == null ||
                board.board[row - stem][col + branch].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - stem, col + branch),
                    null));
        }
        if (row - stem > 0 && col - branch > 0 && (board.board[row - stem][col - branch] == null ||
                board.board[row - stem][col - branch].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - stem, col - branch),
                    null));
        }
        if (row + branch < 9 && col + stem < 9 && (board.board[row + branch][col + stem] == null ||
                board.board[row + branch][col + stem].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row+branch, col+stem),
                    null));
        }
        if (row + branch < 9 && col - stem > 0 && (board.board[row + branch][col - stem] == null ||
                board.board[row + branch][col - stem].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row+branch, col-stem),
                    null));
        }
        if (row - branch > 0 && col + stem < 9 && (board.board[row - branch][col + stem] == null ||
                board.board[row - branch][col + stem].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - branch, col + stem),
                    null));
        }
        if (row - branch > 0 && col - stem > 0 && (board.board[row - branch][col - stem] == null ||
                board.board[row - branch][col - stem].pieceColor != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - branch, col - stem),
                    null));
        }
        return moves;
    }

    public Collection<ChessMove> whitePawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if (board.board[myPosition.row + 1][myPosition.col] == null) {
            if (myPosition.row == 2 && board.board[myPosition.row + 2][myPosition.col] == null) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 2, myPosition.col), null));
            }
            if (myPosition.row + 1 == 8) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col), ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col), ChessPiece.PieceType.KNIGHT));

            } else {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col), null));
            }
        }
        if (myPosition.col != 8 && board.board[myPosition.row + 1][myPosition.col + 1] != null &&
                board.board[myPosition.row + 1][myPosition.col + 1].getTeamColor() != myColor) {
            if (myPosition.row + 1 == 8) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                        ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                        ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                        ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                        ChessPiece.PieceType.KNIGHT));
            } else {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                        null));
            }
        }
        if (myPosition.col != 1 && board.board[myPosition.row + 1][myPosition.col - 1] != null &&
                board.board[myPosition.row + 1][myPosition.col - 1].getTeamColor() != myColor) {
            if (myPosition.row + 1 == 8) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                        ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                        ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                        ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                        ChessPiece.PieceType.KNIGHT));
            } else {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                        null));
            }
        }
        return moves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if (myColor == ChessGame.TeamColor.WHITE) {
            moves.addAll(whitePawnMoves(board, myPosition));
        }
        else {
            if (board.board[myPosition.row - 1][myPosition.col] == null) {
                if (myPosition.row == 7 && board.board[myPosition.row - 2][myPosition.col] == null) {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 2, myPosition.col), null));
                }
                if (myPosition.row - 1 == 1) {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col), ChessPiece.PieceType.KNIGHT));

                } else {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col), null));
                }
            }
            if (myPosition.col != 8 && board.board[myPosition.row - 1][myPosition.col + 1] != null &&
                    board.board[myPosition.row - 1][myPosition.col + 1].getTeamColor() != myColor) {
                if (myPosition.row - 1 == 1) {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                            ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                            ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                            ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                            ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                            null));
                }
            }
            if (myPosition.col != 1 && board.board[myPosition.row - 1][myPosition.col - 1] != null &&
                    board.board[myPosition.row - 1][myPosition.col - 1].getTeamColor() != myColor) {
                if (myPosition.row - 1 == 1) {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                            ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                            ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                            ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                            ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition,
                            new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                            null));
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int i = 0;
        int j = 0;
        boolean enemyPiece = false;
        do {
            if (enemyPiece) {
                break;
            }
            if (board.board[myPosition.row + i][myPosition.col + j] != null &&
                    board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                enemyPiece = true;
            }
            if (i != 0) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + i, myPosition.col + j), null));
            }
            i++;
            j++;
        }
        while ((myPosition.row + i < 9 && myPosition.col + j < 9) &&
                (board.board[myPosition.row + i][myPosition.col + j] == null ||
                        (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor &&
                                !enemyPiece)));
        i = 0;
        j = 0;
        enemyPiece = false;
        do {
            if (enemyPiece) {
                break;
            }
            if (i != 0) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + i, myPosition.col + j), null));
            }
            if (board.board[myPosition.row + i][myPosition.col + j] != null &&
                    board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                enemyPiece = true;
            }
            i++;
            j--;
        }
        while ((myPosition.row + i < 9 && myPosition.col + j > 0) &&
                (board.board[myPosition.row + i][myPosition.col + j] == null ||
                        (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor &&
                                !enemyPiece)));
        i = 0;
        j = 0;
        enemyPiece = false;
        do {
            if (enemyPiece) {
                break;
            }
            if (i != 0) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row + i, myPosition.col + j),
                        null));
            }
            if (board.board[myPosition.row + i][myPosition.col + j] != null &&
                    board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                enemyPiece = true;
            }
            i--;
            j++;
        }
        while ((myPosition.row + i > 0 && myPosition.col + j < 9) &&
                (board.board[myPosition.row + i][myPosition.col + j] == null ||
                        (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor &&
                                !enemyPiece)));
        i = 0;
        j = 0;
        enemyPiece = false;
        do {
            if (enemyPiece) {
                break;
            }
            if (i != 0) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.row + i, myPosition.col + j),
                        null));
            }
            if (board.board[myPosition.row + i][myPosition.col + j] != null &&
                    board.board[myPosition.row + i][myPosition.col + j].pieceColor != myColor) {
                enemyPiece = true;
            }
            i--;
            j--;
        }
        while ((myPosition.row + i > 0 && myPosition.col + j > 0) &&
                (board.board[myPosition.row + i][myPosition.col + j] == null ||
                        (board.board[myPosition.row + i][myPosition.col + j].getTeamColor() != myColor &&
                                !enemyPiece)));
        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int k = 0;
        int w = 0;
        boolean otherPiece = false;
        do {
            if (otherPiece) {
                break;
            }
            if (board.board[myPosition.row + k][myPosition.col + w] != null &&
                    board.board[myPosition.row + k][myPosition.col + w].pieceColor != myColor) {
                otherPiece = true;
            }
            if (k != 0) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + k, myPosition.col + w), null));
            }
            k++;
        }
        while ((myPosition.row + k < 9 && myPosition.col + w < 9) &&
                (board.board[myPosition.row + k][myPosition.col + w] == null ||
                        (board.board[myPosition.row + k][myPosition.col + w].getTeamColor() != myColor &&
                                !otherPiece)));
        k = 0;
        otherPiece = false;
        do {
            if (otherPiece) {
                break;
            }
            if (k != 0) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + k, myPosition.col + w), null));
            }
            if (board.board[myPosition.row + k][myPosition.col + w] != null &&
                    board.board[myPosition.row + k][myPosition.col + w].pieceColor != myColor) {
                otherPiece = true;
            }
            k--;
        }
        while ((myPosition.row + k > 0 && myPosition.col + w > 0) &&
                (board.board[myPosition.row + k][myPosition.col + w] == null ||
                        (board.board[myPosition.row + k][myPosition.col + w].getTeamColor() != myColor &&
                                !otherPiece)));
        k = 0;
        otherPiece = false;
        do {
            if (otherPiece) {
                break;
            }
            if (w != 0) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + k, myPosition.col + w), null));
            }
            if (board.board[myPosition.row + k][myPosition.col + w] != null &&
                    board.board[myPosition.row + k][myPosition.col + w].pieceColor != myColor) {
                otherPiece = true;
            }
            w++;
        }
        while ((myPosition.row + k > 0 && myPosition.col + w < 9) &&
                (board.board[myPosition.row + k][myPosition.col + w] == null ||
                        (board.board[myPosition.row + k][myPosition.col + w].getTeamColor() != myColor &&
                                !otherPiece)));
        w = 0;
        otherPiece = false;
        do {
            if (otherPiece) {
                break;
            }
            if (w != 0) {
                moves.add(new ChessMove(myPosition,
                        new ChessPosition(myPosition.row + k, myPosition.col + w), null));
            }
            if (board.board[myPosition.row + k][myPosition.col + w] != null &&
                    board.board[myPosition.row + k][myPosition.col + w].pieceColor != myColor) {
                otherPiece = true;
            }
            w--;
        }
        while ((myPosition.row + k > 0 && myPosition.col + w > 0) &&
                (board.board[myPosition.row + k][myPosition.col + w] == null ||
                        (board.board[myPosition.row + k][myPosition.col + w].getTeamColor() != myColor &&
                                !otherPiece)));
        return moves;
    }
}
