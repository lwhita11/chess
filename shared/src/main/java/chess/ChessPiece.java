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
    private boolean hasMoved = false;
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
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        switch(board.getPiece(myPosition).pieceType) {
            case KING:
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
                break;
            case BISHOP:
                moves.addAll(bishopMoves(board, myPosition));
                break;
            case ROOK:
                moves.addAll(rookMoves(board, myPosition));
                break;
            case KNIGHT:
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
                break;
            case QUEEN:
                moves.addAll(bishopMoves(board, myPosition));
                moves.addAll(rookMoves(board, myPosition));
                break;
            case PAWN:
                if (myColor == ChessGame.TeamColor.WHITE) {
                    if (board.board[myPosition.row + 1][myPosition.col] == null) {
                        if (myPosition.row == 2 && board.board[myPosition.row + 2][myPosition.col] == null) {
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 2, myPosition.col), null));
                        }
                        if (myPosition.row + 1 == 8) {
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col), PieceType.KNIGHT));

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
                                    PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                                    PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                                    PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col + 1),
                                    PieceType.KNIGHT));
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
                                    PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                                    PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                                    PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                                    PieceType.KNIGHT));
                        } else {
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row + 1, myPosition.col - 1),
                                    null));
                        }
                    }
                }
                else {
                    if (board.board[myPosition.row - 1][myPosition.col] == null) {
                        if (myPosition.row == 7 && board.board[myPosition.row - 2][myPosition.col] == null) {
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 2, myPosition.col), null));
                        }
                        if (myPosition.row - 1 == 1) {
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col), PieceType.KNIGHT));

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
                                    PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                                    PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                                    PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col + 1),
                                    PieceType.KNIGHT));
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
                                    PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                                    PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                                    PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                                    PieceType.KNIGHT));
                        } else {
                            moves.add(new ChessMove(myPosition,
                                    new ChessPosition(myPosition.row - 1, myPosition.col - 1),
                                    null));
                        }
                    }
                }
                break;
        }
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
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

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
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
