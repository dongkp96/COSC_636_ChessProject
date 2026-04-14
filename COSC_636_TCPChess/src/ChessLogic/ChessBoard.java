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

    public ChessPiece getPiece(int row, int col){
        if(row < 0 || row >= 8 || col < 0 || col >= 8){
            return null;
        }
        return board[row][col];
    }

    /*
     * Method used to check whether a given row and column are inside the board
     * Returns true if the position is valid, otherwise false
     */
    public boolean isInsideBoard(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /*
     * Method used to check whether a given square is empty
     * A square is considered empty if it contains a NEUTRAL TILE piece
     */
    public boolean isEmpty(int row, int col){
        if(!isInsideBoard(row, col)){
            return false;
        }

        ChessPiece piece = board[row][col];
        return piece.getColor() == Color.NEUTRAL && piece.getType() == PieceType.TILE;
    }

    /*
     * Method used to place a ChessPiece at a given row and column
     * Throws an error if the position is outside the board
     */
    public void setPiece(int row, int col, ChessPiece piece){
        if(!isInsideBoard(row, col)){
            throw new IllegalArgumentException("Board position out of bounds");
        }
        board[row][col] = piece;
    }

    /*
     * Method used to move a piece from one square to another
     * This method does not check whether the move is legal
     * It only updates the board positions
     */
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){
        if(!isInsideBoard(fromRow, fromCol) || !isInsideBoard(toRow, toCol)){
            throw new IllegalArgumentException("Move out of bounds");
        }

        ChessPiece movingPiece = board[fromRow][fromCol];
        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = new ChessPiece(Color.NEUTRAL, PieceType.TILE);
    }

    /*
     * Method used to check whether a given square contains a friendly piece
     * Returns true if the square contains a piece of the given color
     */
    public boolean hasFriendlyPiece(int row, int col, Color color){
        if(!isInsideBoard(row, col) || isEmpty(row, col)){
            return false;
        }

        return board[row][col].getColor() == color;
    }

    /*
     * Method used to check whether a given square contains an enemy piece
     * Returns true if the square contains a piece of the opposite color
     */
    public boolean hasEnemyPiece(int row, int col, Color color){
        if(!isInsideBoard(row, col) || isEmpty(row, col)){
            return false;
        }

        return board[row][col].getColor() != color;
    }

    public void updateBoard(String move){


    }
}
