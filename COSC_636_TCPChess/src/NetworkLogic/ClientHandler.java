package NetworkLogic;
import java.net.Socket;
import ChessLogic.GameSession;

public class ClientHandler implements Runnable{

    private Socket socket;
    private ClientHandler opponent;
    private GameSession game;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

    }

    public void setOpponent(ClientHandler opponent){
        this.opponent = opponent;
    }

    public void setGameSession(GameSession game){
        this.game = game;
    }
}
