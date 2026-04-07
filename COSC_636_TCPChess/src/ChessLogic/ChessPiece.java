package ChessLogic;

public class ChessPiece {

    private Color color;
    private PieceType type;

    public ChessPiece(Color color, PieceType type){
        this.color = color;
        this.type = type;
    }

    /**
     *
     * @return Color enum representing the faction of the piece
     * can be: BLACK, WHITE, NEUTRAL (for empty tile)
     */
    public Color getColor(){
        return this.color;
    }

    /**
     *
     * @return PieceType enum used to identify the type of
     * chess piece it is. Planned to be used with move validation
     */
    public PieceType getType(){
        return this.type;
    }

    /**
     * @return String representing the symbol to be used for the 2D
     * chess board display by building it based on the ChessPiece's type
     * and Color
     */
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
    /**
     *
     * @return String representation of the color and type of the chess piece
     */
    @Override
    public String toString(){
        return this.color + " " + this.type;
    }
}
