package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class PrintBoard {
    public static String boardToStringBlack(ChessBoard board) {
        String row = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "\t h\t g\t f\t e\t d\t c\t b\t a\t\t " + EscapeSequences.RESET_BG_COLOR + "\n";
        List<String> rows = new ArrayList<>();
        String row1 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "1\t";
        String row2 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "2\t";
        String row3 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "3\t";
        String row4 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "4\t";
        String row5 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "5\t";
        String row6 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "6\t";
        String row7 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "7\t";
        String row8 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "8\t";

        String result = row;

        rows.add(row1);rows.add(row2);rows.add(row3);rows.add(row4);rows.add(row5);rows.add(row6);rows.add(row7);
        rows.add(row8);
        result = iterateBoard(0, 7, 1,-1, board, rows, result);
        return result + row + EscapeSequences.RESET_BG_COLOR;
    }

    private static String iterateBoard(int iVal, int jVal, int iIter, int jIter, ChessBoard board, List<String> rows, String result) {
        for (int i = iVal; (iVal > 0 ? i >= 0 : i < 8); i += iIter) {
            for (int j = jVal; (jVal > 0 ? j >= 0 : j < 8); j += jIter) {
                String background;
                ChessPosition currPos = new ChessPosition(i + 1, j + 1);
                ChessPiece currPiece = board.getPiece(currPos);
                if ((i + j) % 2 == 0) {
                    background = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
                } else {
                    background = EscapeSequences.SET_BG_COLOR_WHITE;
                }
                if (currPiece == null) {
                    rows.set(i, rows.get(i) + background + "\t");
                    continue;
                }
                ChessGame.TeamColor pieceColor = currPiece.getTeamColor();
                if (pieceColor == ChessGame.TeamColor.BLACK) {
                    switch (currPiece.getPieceType()) {
                        case ChessPiece.PieceType.PAWN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_PAWN + "\t");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_ROOK + "\t");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_KNIGHT + "\t");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_BISHOP + "\t");
                            break;
                        case ChessPiece.PieceType.KING:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_KING + "\t");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.BLACK_QUEEN + "\t");
                            break;
                    }
                }

                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    switch (currPiece.getPieceType()) {
                        case ChessPiece.PieceType.PAWN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_PAWN + "\t");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_ROOK + "\t");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_KNIGHT + "\t");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_BISHOP + "\t");
                            break;
                        case ChessPiece.PieceType.KING:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_KING + "\t");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            rows.set(i, rows.get(i) + background + EscapeSequences.WHITE_QUEEN + "\t");
                            break;
                    }
                }
            }
            int rightVal;
            if (jVal == 0) {
                rightVal = 7 - i;
            }
            else{
                rightVal = i;
            }
            result = result + rows.get(i) + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "\t" + Integer.toString(i + 1)
                    + EscapeSequences.RESET_BG_COLOR + "\n";
        }
        return result;
    }

    public static String boardToStringWhite(ChessBoard board) {
        String row = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "\t a\t b\t c\t d\t e\t f\t g\t h\t\t " + EscapeSequences.RESET_BG_COLOR + "\n";
        List<String> rows = new ArrayList<>();
        String row1 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "1\t";
        String row2 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "2\t";
        String row3 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "3\t";
        String row4 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "4\t";
        String row5 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "5\t";
        String row6 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "6\t";
        String row7 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "7\t";
        String row8 = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "8\t";

        String result = row;

        rows.add(row1);rows.add(row2);rows.add(row3);rows.add(row4);rows.add(row5);rows.add(row6);rows.add(row7);
        rows.add(row8);
        result = iterateBoard(7, 0, -1,1, board, rows, result);
        return result + row + EscapeSequences.RESET_BG_COLOR;
    }

}
