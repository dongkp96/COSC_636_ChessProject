package NetworkLogic;
import java.net.Socket;
import ChessLogic.GameSession;

public class ChessClient implements Runnable{

    private Socket socket;
    private ChessClient opponent;
    private GameSession game;

    public ChessClient(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

    }

    public void setOpponent(ChessClient opponent){

    }

    public void setGameSession(GameSession game){
        this.game = game;
    }
}
