package ChessLogic;

import java.io.PrintWriter;

public class GameSession {

    private Color currentTurn;
    private ChessBoard board;

    public GameSession(){
        this.currentTurn = Color.WHITE;
        this.board = new ChessBoard();
    }

    /**
     * Plan is to use this to make players wait their turns based on their
     * Color enum
     *
     * @param color used to check with the internal currentTurn flag
     * to check if the wait() method should be called
     *
     */
    public synchronized void checkTurn(Color color, PrintWriter writer){
        try{
            while(currentTurn != color){
                writer.println("Waiting, current it is " + this.getCurrentTurn() + "'s turn");
                wait();
            }
        }catch(InterruptedException e){
            System.out.println("Error while waiting for turn: " + e);
        }
    }

    /**
     *
     * @return Color enum, serves as a getter for this class
     */

    public synchronized void switchTurn(){
        this.currentTurn = (this.currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
        notifyAll();
    }

    public synchronized Color getCurrentTurn(){
        return this.currentTurn;

    }

    public synchronized String getCurrentBoard(){
        return board.getBoard();
    }
    
        /**
     * Method used to process a move entered as a String
     * The move is expected in the format: fromRow fromCol toRow toCol
     * Example: "6 4 4 4"
     *
     * @param move String entered and to be processed to identify the move choice
     *             and for validation
     * @return String indicating whether the move was valid or why it failed
     */
    public synchronized String ProcessMove(String move){
        if(move == null || move.trim().isEmpty()){
            return "Invalid move: empty input";
        }

        String[] parts = move.trim().split("\\s+");

        if(parts.length != 4){
            return "Invalid move format. Use: fromRow fromCol toRow toCol";
        }

        int fromRow;
        int fromCol;
        int toRow;
        int toCol;

        try{
            fromRow = Integer.parseInt(parts[0]);
            fromCol = Integer.parseInt(parts[1]);
            toRow = Integer.parseInt(parts[2]);
            toCol = Integer.parseInt(parts[3]);
        }catch(NumberFormatException e){
            return "Invalid move format. Rows and columns must be numbers";
        }

        if(!board.isInsideBoard(fromRow, fromCol) || !board.isInsideBoard(toRow, toCol)){
            return "Invalid move: position out of bounds";
        }

        if(board.isEmpty(fromRow, fromCol)){
            return "Invalid move: no piece at starting position";
        }

        ChessPiece piece = board.getPiece(fromRow, fromCol);

        if(piece.getColor() != currentTurn){
            return "Invalid move: it is " + currentTurn + "'s turn";
        }

        if(!isValidMove(piece, fromRow, fromCol, toRow, toCol)){
            return "Invalid move for selected piece";
        }

        board.movePiece(fromRow, fromCol, toRow, toCol);
        switchTurn();

        return "VALID";
    }

     /**
     * Method used to determine whether a move is valid for the selected piece
     *
     * @param piece the ChessPiece being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the move is valid for that piece, otherwise false
     */
    public boolean isValidMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        if(piece == null){
            return false;
        }

        if(board.hasFriendlyPiece(toRow, toCol, piece.getColor())){
            return false;
        }

        switch(piece.getType()){
            case PAWN:
                return isValidPawnMove(piece, fromRow, fromCol, toRow, toCol);
            case ROOK:
                return isValidRookMove(piece, fromRow, fromCol, toRow, toCol);
            case KNIGHT:
                return isValidKnightMove(piece, fromRow, fromCol, toRow, toCol);
            case BISHOP:
                return isValidBishopMove(piece, fromRow, fromCol, toRow, toCol);
            case QUEEN:
                return isValidQueenMove(piece, fromRow, fromCol, toRow, toCol);
            case KING:
                return isValidKingMove(piece, fromRow, fromCol, toRow, toCol);
            default:
                return false;
        }
    }

    /**
     * Method used to validate whether a pawn move is legal
     *
     * @param piece the pawn being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the pawn move is valid, otherwise false
     */
    public boolean isValidPawnMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        int direction;
        int startRow;

        if(piece.getColor() == Color.WHITE){
            direction = -1;
            startRow = 6;
        }
        else if(piece.getColor() == Color.BLACK){
            direction = 1;
            startRow = 1;
        }
        else{
            return false;
        }

        // moving forward one square
        if(toCol == fromCol && toRow == fromRow + direction && board.isEmpty(toRow, toCol)){
            return true;
        }

        // moving forward two squares from starting position
        if(toCol == fromCol && fromRow == startRow && toRow == fromRow + (2 * direction)){
            if(board.isEmpty(fromRow + direction, fromCol) && board.isEmpty(toRow, toCol)){
                return true;
            }
        }

        // capturing diagonally
        if(Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction){
            if(board.hasEnemyPiece(toRow, toCol, piece.getColor())){
                return true;
            }
        }

        return false;
    }

    /**
     * Method used to validate whether a rook move is legal
     *
     * @param piece the rook being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the rook move is valid, otherwise false
     */
    public boolean isValidRookMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        // rook must move only in a straight line
        if(fromRow != toRow && fromCol != toCol){
            return false;
        }

        // moving horizontally
        if(fromRow == toRow){
            int step = (toCol > fromCol) ? 1 : -1;

            for(int col = fromCol + step; col != toCol; col += step){
                if(!board.isEmpty(fromRow, col)){
                    return false;
                }
            }
        }

        // moving vertically
        if(fromCol == toCol){
            int step = (toRow > fromRow) ? 1 : -1;

            for(int row = fromRow + step; row != toRow; row += step){
                if(!board.isEmpty(row, fromCol)){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Method used to validate whether a knight move is legal
     *
     * @param piece the knight being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the knight move is valid, otherwise false
     */
    public boolean isValidKnightMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    /**
     * Method used to validate whether a bishop move is legal
     *
     * @param piece the bishop being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the bishop move is valid, otherwise false
     */
    public boolean isValidBishopMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        // bishop must move diagonally
        if(rowDiff != colDiff){
            return false;
        }

        int rowStep = (toRow > fromRow) ? 1 : -1;
        int colStep = (toCol > fromCol) ? 1 : -1;

        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        while(currentRow != toRow && currentCol != toCol){
            if(!board.isEmpty(currentRow, currentCol)){
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }
    
        /**
     * Method used to validate whether a queen move is legal
     *
     * @param piece the queen being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the queen move is valid, otherwise false
     */
    public boolean isValidQueenMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        return isValidRookMove(piece, fromRow, fromCol, toRow, toCol) ||
               isValidBishopMove(piece, fromRow, fromCol, toRow, toCol);
    }

    /**
     * Method used to validate whether a king move is legal
     *
     * @param piece the king being moved
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return true if the king move is valid, otherwise false
     */
    public boolean isValidKingMove(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol){
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        return rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);
    }
}
