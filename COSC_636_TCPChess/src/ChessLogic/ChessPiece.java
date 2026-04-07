package ChessLogic;

public class ChessPiece {

    protected Color color;
    protected PieceType type;

    public ChessPiece(Color color, PieceType type){
        this.color = color;
        this.type = type;
    }

    @Override
    public String toString(){
        return this.color + " " + this.type;
    }

    /*
    * Method that returns the notation for the chess piece
    * that is used for the board display. This method is used in
    * the chessboard's displayBoard() method
    * */
    public String getSymbol(){
        StringBuilder builder = new StringBuilder();
        switch(this.color){
            case BLACK:
                builder.append("B");
                break;
            case WHITE:
                builder.append("W");
                break;
            case NEUTRAL:
                builder.append("O");
                break;
        }

        switch(this.type){
            case PAWN:
                builder.append("P");
                break;
            case KNIGHT:
                builder.append("G");
                break;
            case ROOK:
                builder.append("R");
                break;
            case BISHOP:
                builder.append("B");
                break;
            case QUEEN:
                builder.append("Q");
                break;
            case KING:
                builder.append("K");
                break;
        }

        return builder.toString();


    }
}
