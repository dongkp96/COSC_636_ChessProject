package NetworkLogic;

import java.io.IOException;

import ChessLogic.Color;
import ChessLogic.GameSession;

public class MatchMaker implements Runnable {
     public void run() {
        while (true) {
            try {
                /*ClientHandler p1 = ChessServer.autoQueue.take();
                ClientHandler p2 = ChessServer.autoQueue.take();*/

                ClientHandler p1 = ChessServer.takeFromQueue();
                ClientHandler p2 = ChessServer.takeFromQueue();

                if (p1.isInGame()){
                    ChessServer.putInQueue(p2);
                    continue;

                }
                if (p2.isInGame()) { 
                    ChessServer.putInQueue(p1); //put p1 back in the queue
                    continue;
                }

                ChessServer.startGame(p1, p2);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

