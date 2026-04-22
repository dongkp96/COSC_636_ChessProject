import ChessLogic.*;
import NetworkLogic.ChessClient;
import NetworkLogic.ChessServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ChessServer server = new ChessServer(50);

        Thread thread1 = new Thread(() ->{
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread1.start();
        Thread.sleep(500);
        ChessClient.main();


    }
}