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
                        new ChessPiece(Color.WHITE, PieceType.QUEEN),new ChessPiece(Color.WHITE,
                        PieceType.KING),new ChessPiece(Color.WHITE, PieceType.BISHOP),
                        new ChessPiece(Color.WHITE, PieceType.KNIGHT),new ChessPiece(Color.WHITE,
                        PieceType.ROOK)}
        };

    }

    /*
    * Method used to display the chessBoard state in the following state
           a   b   c   d   e   f   g   h
    8     BR  BG  BB  BQ  BK  BB  BG  BR
    7     BP  BP  BP  BP  BP  BP  BP  BP
    6     O   O   O   O   O   O   O   O
    5     O   O   O   O   O   O   O   O
    4     O   O   O   O   O   O   O   O
    3     O   O   O   O   O   O   O   O
    2     WP  WP  WP  WP  WP  WP  WP  WP
    1     WR  WG  WB  WQ  WK  WB  WG  WR
    * Once the server and client is finished, this should return a string instead IDK
    * */
    public String getBoard(){
        StringBuilder sb = new StringBuilder();

 // Column headers: a through h
    sb.append(String.format("%-6s", ""));
    for (int col = 0; col < 8; col++) {
        sb.append(String.format("%-4s", (char)('a' + col)));
    }
    sb.append("|");

    // Row labels: rank 8 at top (row 0), rank 1 at bottom (row 7)
    for (int row = 0; row < 8; row++) {
        int rank = 8 - row;  // row 0 = rank 8, row 7 = rank 1
        sb.append(String.format("%-6s", rank));
        for (int col = 0; col < 8; col++) {
            sb.append(String.format("%-4s", board[row][col].getSymbol()));
        }
        sb.append("|");
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

}
