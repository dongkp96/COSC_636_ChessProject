package NetworkLogic;

public class MatchMaker implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                ClientHandler p1 = ChessServer.takeFromQueue();
                ClientHandler p2 = ChessServer.takeFromQueue();

                if (p1.isInGame()) {
                    ChessServer.putInQueue(p2);
                    continue;
                }

                if (p2.isInGame()) {
                    ChessServer.putInQueue(p1);
                    continue;
                }

                ChessServer.startGame(p1, p2);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}