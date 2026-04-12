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
            A   BR  BG  BB  BQ  BK  BB  BG  BR
            B   BP  BP  BP  BP  BP  BP  BP  BP
            C   O   O   O   O   O   O   O   O
            D   O   O   O   O   O   O   O   O
            E   O   O   O   O   O   O   O   O
            F   O   O   O   O   O   O   O   O
            G   WP  WP  WP  WP  WP  WP  WP  WP
            H   WR  WG  WB  WK  WQ  WB  WG  WR
    * Once the server and client is finished, this should return a string instead IDK
    * */
    public String getBoard(){
        StringBuilder sb = new StringBuilder();

// Print column headers (numbers)
        sb.append(String.format("%-8s", ""));
        for (int col = 0; col < 8; col++) {
            sb.append(String.format("%-4s", col));
        }
        sb.append("|");

// Print each row with letter labels
        char rowLabel = 'A';
        for (int row = 0; row < 8; row++) {
            sb.append(String.format("%-8s", rowLabel));
            for (int col = 0; col < 8; col++) {
                sb.append(String.format("%-4s", board[row][col].getSymbol()));
            }
            sb.append("|");
            rowLabel++;
        }

        return sb.toString();
    }

    public void updateBoard(String move){


    }
}
