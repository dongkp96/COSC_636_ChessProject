package ChessLogic;

import java.util.Arrays;

public class ChessBoard {

    private ChessPiece [][] board;

    public ChessBoard(){
        board = new ChessPiece[][]{
                {new ChessPiece(Color.BLACK, PieceType.ROOK),new ChessPiece(Color.BLACK,
                        PieceType.KNIGHT),new ChessPiece(Color.BLACK, PieceType.BISHOP),
                        new ChessPiece(Color.BLACK, PieceType.QUEEN),new ChessPiece(Color.BLACK,
                        PieceType.KING),new ChessPiece(Color.BLACK, PieceType.BISHOP),
                        new ChessPiece(Color.BLACK, PieceType.KNIGHT),new ChessPiece(Color.BLACK,
                        PieceType.ROOK)},
                {new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN),
                        new ChessPiece(Color.BLACK, PieceType.PAWN)},
                {new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE)},
                {new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE)},
                {new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE)},
                {new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE),
                        new ChessPiece(Color.NEUTRAL, PieceType.TILE)},
                {new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN),
                        new ChessPiece(Color.WHITE, PieceType.PAWN)},
                {new ChessPiece(Color.WHITE, PieceType.ROOK),new ChessPiece(Color.WHITE,
                        PieceType.KNIGHT),new ChessPiece(Color.WHITE, PieceType.BISHOP),
                        new ChessPiece(Color.WHITE, PieceType.KING),new ChessPiece(Color.WHITE,
                        PieceType.QUEEN),new ChessPiece(Color.WHITE, PieceType.BISHOP),
                        new ChessPiece(Color.WHITE, PieceType.KNIGHT),new ChessPiece(Color.WHITE,
                        PieceType.ROOK)}
        };

    }

    /*
    * Method used to display the chessBoard state in the following state
    *           0   1   2   3   4   5   6   7
            0   BR  BK  BB  BQ  BK  BB  BK  BR
            1   BP  BP  BP  BP  BP  BP  BP  BP
            2   O   O   O   O   O   O   O   O
            3   O   O   O   O   O   O   O   O
            4   O   O   O   O   O   O   O   O
            5   O   O   O   O   O   O   O   O
            6   WP  WP  WP  WP  WP  WP  WP  WP
            7   WR  WK  WB  WK  WQ  WB  WK  WR
    * Once the server and client is finished, this should return a string instead IDK
    * */
    public void displayBoard(){
        StringBuilder sb = new StringBuilder();

        // Print column headers
        sb.append(String.format("%-6s", ""));
        for (int col = 0; col < 8; col++) {
            sb.append(String.format("%-4s", col));
        }
        sb.append("\n");

        // Print each row
        for (int row = 0; row < 8; row++) {
            sb.append(String.format("%-6s", row));
            for (int col = 0; col < 8; col++) {
                sb.append(String.format("%-4s", board[row][col].getSymbol()));
            }
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }
}
