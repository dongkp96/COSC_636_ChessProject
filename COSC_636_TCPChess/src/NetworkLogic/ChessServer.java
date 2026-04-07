package NetworkLogic;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ChessLogic.GameSession;

public class ChessServer {
    private boolean gameActive;
    private int port;
    private ServerSocket socket;
    private ClientHandler playerOne;
    private ClientHandler playerTwo;

    public ChessServer(int port){
        this.port = port;
    }

    /**
     * Start() method used to establish the server via the server socket and
     * take connections to client sockets. Pairs the clients together and provides
     * a GameSession object followed by starting the threads
     */
    public void start(){
        try{
            this.socket = new ServerSocket(this.port);
            //establishes Server socket at port

            Socket clientSocket1 = this.socket.accept();
            Socket clientSocket2 = this.socket.accept();
            //calls accept for two sockets to connect

            playerOne = new ClientHandler(clientSocket1);
            playerTwo = new ClientHandler(clientSocket2);
            /*
            * Uses those sockets
            * */

            playerOne.setOpponent(playerTwo);
            playerTwo.setOpponent(playerOne);
            /*
            * Sets each player's clientHandlers with references
            * for each other, so they can message each other
            * */

            GameSession game = new GameSession();
            //creates a chess game Session

            playerOne.setGameSession(game);
            playerTwo.setGameSession(game);
            /*
            * Provides the client's a reference to the game they are playing
            * */

            Thread threadP1 = new Thread(playerOne);
            Thread threadP2 = new Thread(playerTwo);

            threadP1.start();
            threadP2.start();

        }catch(IOException e){
            System.out.println(e);
        }
    }
}
