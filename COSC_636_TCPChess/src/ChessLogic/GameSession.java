package ChessLogic;

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
    public synchronized void checkTurn(Color color){
        try{
            while(currentTurn != color){
                wait();
            }
        }catch(InterruptedException e){
            System.out.println("Error while waiting for turn: " + e);
        }
    }

    /**
     *
     * @param move, String entered and to be processed to identify the move choice
     *              and for validation
     */
    public void ProcessMove(String move){

    }

    /**
     *
     * @return Color enum, serves as a getter for this class
     */
    public synchronized Color getCurrentTurn(){
        return this.currentTurn;

    }
}
