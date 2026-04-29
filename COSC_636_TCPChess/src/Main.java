import NetworkLogic.ChessServer;

public class Main {
    public static void main(String[] args) {
        try {
            ChessServer server = new ChessServer(5000);
            server.start();
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}