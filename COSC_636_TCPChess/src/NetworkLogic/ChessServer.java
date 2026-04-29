package NetworkLogic;

import ChessLogic.Color;
import ChessLogic.GameSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ChessServer {
    private final int port;
    private ServerSocket serverSocket;

    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ClientHandler> waitingList = new ConcurrentHashMap<>();
    private static final BlockingQueue<ClientHandler> autoQueue = new LinkedBlockingQueue<>();

    public ChessServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Chess Server started on port " + port);

        Thread matchMakerThread = new Thread(new MatchMaker());
        matchMakerThread.start();

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            Thread clientThread = new Thread(handler);
            clientThread.start();
        }
    }

    public static synchronized boolean registerClient(String username, ClientHandler handler) {
        if (clients.containsKey(username)) {
            return false;
        }

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

    public static void enterAutoQueue(ClientHandler player) throws InterruptedException {
        removeFromQueues(player);
        autoQueue.put(player);
    }

    public static ClientHandler takeFromQueue() throws InterruptedException {
        return autoQueue.take();
    }

    public static void putInQueue(ClientHandler handler) throws InterruptedException {
        autoQueue.put(handler);
    }

    public static void enterWaitingList(ClientHandler player) {
        removeFromQueues(player);
        waitingList.put(player.getUsername(), player);
    }

    public static String getWaitingList(String requester) {
        StringBuilder sb = new StringBuilder();

        sb.append("WAITING PLAYERS: ");

        for (String name : waitingList.keySet()) {
            if (!name.equals(requester)) {
                sb.append(name).append(" ");
            }
        }

        return sb.toString();
    }

    public static ClientHandler getClient(String username) {
        return clients.get(username);
    }

    public static synchronized boolean startMatch(ClientHandler challenger, String opponentName) {
        ClientHandler opponent = waitingList.get(opponentName);

        if (opponent == null || opponent.isInGame()) {
            return false;
        }

        opponent.setPendingChallenger(challenger.getUsername());
        opponent.sendMessage("CHALLENGE: " + challenger.getUsername() + " wants to play. Type ACCEPT or REJECT.");
        challenger.sendMessage("Challenge sent to " + opponentName + ". Waiting for response...");

        return true;
    }

    public static synchronized void startGame(ClientHandler p1, ClientHandler p2) {
        removeFromQueues(p1);
        removeFromQueues(p2);

        GameSession game = new GameSession();

        p1.setOpponent(p2);
        p2.setOpponent(p1);

        if (Math.random() > 0.5) {
            p1.setColor(Color.WHITE);
            p2.setColor(Color.BLACK);
        } else {
            p1.setColor(Color.BLACK);
            p2.setColor(Color.WHITE);
        }

        p1.setGameSession(game);
        p2.setGameSession(game);

        p1.setInGame(true);
        p2.setInGame(true);

        p1.sendMessage("MATCH_STARTED: Opponent is " + p2.getUsername());
        p2.sendMessage("MATCH_STARTED: Opponent is " + p1.getUsername());
    }

    private static void removeFromQueues(ClientHandler player) {
        autoQueue.remove(player);

        if (player.getUsername() != null) {
            waitingList.remove(player.getUsername());
        }
    }
}