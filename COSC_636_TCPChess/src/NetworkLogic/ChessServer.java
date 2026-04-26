package NetworkLogic;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import ChessLogic.Color;
import ChessLogic.GameSession;


public class ChessServer {
    private final int port;
    private ServerSocket serverSocket;

  // All connected clients
    private static ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    // Manual matchmaking list
    private static ConcurrentHashMap<String, ClientHandler> waitingList = new ConcurrentHashMap<>();

    // Auto matchmaking queue
    private static BlockingQueue<ClientHandler> autoQueue = new LinkedBlockingQueue<>();

    public ChessServer(int port){
        this.port = port;
    }

    /**
     * Start() method used to establish the server via the server socket and
     * take connections to client sockets. Pairs the clients together and provides
     * a GameSession object followed by starting the threads
     */
    public void start() throws IOException {
      
        serverSocket = new ServerSocket(port);
        System.out.println("Chess Server started on port " + port);

        // Start matchmaking thread
        new Thread(new MatchMaker()).start();

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            new Thread(handler).start();
        }
    }
      
      //Client Managers 

    public static synchronized boolean registerClient(String username, ClientHandler handler) {
        if (clients.containsKey(username)) return false;
        clients.put(username, handler);
        return true;
    }

    public static void removeClient(ClientHandler player) {
        if (player.getUsername() != null) {
            clients.remove(player.getUsername());
            waitingList.remove(player.getUsername());
        }
        autoQueue.remove(player);
    }
      // Matchmaking
    //Supports auto queuing and a waiting list
    public static void enterAutoQueue(ClientHandler player) throws InterruptedException {
        removeFromQueues(player);
        autoQueue.put(player);
    }

    /**
     * Method used to obtain a ClientHandler from autoQueue
     */
    public static ClientHandler takeFromQueue() throws InterruptedException {
        return autoQueue.take();
    }

    /**
    * Method used to place a ClientHandler back into the autoQueue
    * */
    public static void putInQueue(ClientHandler handler) throws InterruptedException{
        autoQueue.put(handler);
    }


    public static void enterWaitingList(ClientHandler player) {
        removeFromQueues(player);
        waitingList.put(player.getUsername(), player);
    }

    /**
     *Method used to obtain String version of the waiting list
     *@Returns String of the waiting list to be used to send to Clients
    */
    public static String getWaitingList(String requester) {
        StringBuilder sb = new StringBuilder("WAITING PLAYERS: ");
        for (String name : waitingList.keySet()) {
            if (!name.equals(requester)) {
                sb.append(name).append(" ");
            }
        }
        return sb.toString();
    }

    public  static synchronized boolean startMatch(ClientHandler p1, String opponentName) {
        ClientHandler p2 = waitingList.get(opponentName);

        if (p2 == null || p2.isInGame()) return false;

        removeFromQueues(p1);
        removeFromQueues(p2);

        startGame(p1, p2);
        return true;
    }

    public static synchronized void startGame(ClientHandler p1, ClientHandler p2) {
        GameSession game = new GameSession();

        p1.setOpponent(p2);
        p2.setOpponent(p1);
        double random = Math.random();

        if(random > 0.5){
            p1.setColor(Color.WHITE);
            p2.setColor(Color.BLACK);
        }else{
            p1.setColor(Color.BLACK);
            p2.setColor(Color.WHITE);
        }

        p1.setGameSession(game);
        p2.setGameSession(game);

        p1.setInGame(true);
        p2.setInGame(true);

        p1.sendMessage("MATCH_STARTED " + p2.getUsername());
        p2.sendMessage("MATCH_STARTED " + p1.getUsername());
    }

    private static void removeFromQueues(ClientHandler player) {
        autoQueue.remove(player);
        if (player.getUsername() != null) {
            waitingList.remove(player.getUsername());
        }
    }
}
      
      
      