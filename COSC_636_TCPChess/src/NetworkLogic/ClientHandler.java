package NetworkLogic;

import ChessLogic.Color;
import ChessLogic.GameSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    private ClientHandler opponent;
    private GameSession game;
    private String username;
    private Color color;

    private BufferedReader reader;
    private PrintWriter writer;

    private volatile boolean inGame = false;
    private volatile String pendingChallenger = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            setupUsername();
            runLobby();

            if (inGame) {
                runGame();
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());

        } finally {
            ChessServer.removeClient(this);

            if (opponent != null) {
                opponent.notifyOpponentQuit();
            }

            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void setupUsername() throws IOException {
        while (true) {
            writer.println("Enter username:");

            String name = reader.readLine();

            if (name == null) {
                throw new IOException("Client disconnected during username setup.");
            }

            name = name.trim();

            if (name.isBlank()) {
                writer.println("ERROR: Username cannot be blank.");
                continue;
            }

            if (!ChessServer.registerClient(name, this)) {
                writer.println("ERROR: Username already taken.");
                continue;
            }

            this.username = name;
            writer.println("VALID: Username set to " + username);
            break;
        }
    }

    private void runLobby() throws IOException {
        sendMenu();

        while (!inGame) {
            String input = reader.readLine();

            if (input == null) {
                throw new IOException("Client disconnected in lobby.");
            }

            input = input.trim();

            if (input.isBlank()) {
                writer.println("ERROR: Command cannot be blank.");
                continue;
            }

            String[] parts = input.split("\\s+");
            String command = parts[0].toUpperCase();

            switch (command) {
                case "AUTO":
                    try {
                        ChessServer.enterAutoQueue(this);
                        writer.println("Entered auto queue. Wait for another player.");

                        while (!inGame) {
                            sleep(200);
                        }

                        return;

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        writer.println("ERROR: Could not enter auto queue.");
                        return;
                    }

                case "WAIT":
                    ChessServer.enterWaitingList(this);
                    writer.println("Added to waiting list.");
                    break;

                case "LIST":
                    writer.println(ChessServer.getWaitingList(username));
                    break;

                case "PLAY":
                    if (parts.length < 2) {
                        writer.println("ERROR: Use PLAY username");
                        break;
                    }

                    boolean success = ChessServer.startMatch(this, parts[1]);

                    if (!success) {
                        writer.println("ERROR: Opponent not available.");
                        break;
                    }

                    while (!inGame) {
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            writer.println("ERROR: Challenge wait was interrupted.");
                            return;
                        }
                    }

                    return;

                case "ACCEPT":
                    acceptChallenge();

                    if (inGame) {
                        return;
                    }

                    break;

                case "REJECT":
                    rejectChallenge();
                    break;

                case "CHECK":
                    if (inGame) {
                        writer.println("MATCH_STARTED: Opponent is " + opponent.getUsername());
                        return;
                    } else {
                        writer.println("Still waiting...");
                    }
                    break;

                case "MENU":
                    sendMenu();
                    break;

                case "QUIT":
                    writer.println("Goodbye.");
                    return;

                default:
                    writer.println("ERROR: Invalid command.");
                    sendMenu();
                    break;
            }
        }
    }

    private void sendMenu() {
        writer.println("Commands: AUTO | WAIT | LIST | PLAY username | ACCEPT | REJECT | CHECK | MENU | QUIT");
    }

    private void acceptChallenge() {
        if (pendingChallenger == null) {
            writer.println("ERROR: No pending challenge.");
            return;
        }

        ClientHandler challenger = ChessServer.getClient(pendingChallenger);

        if (challenger == null) {
            writer.println("ERROR: Challenger disconnected.");
            pendingChallenger = null;
            return;
        }

        ChessServer.startGame(challenger, this);
        pendingChallenger = null;
    }

    private void rejectChallenge() {
        if (pendingChallenger == null) {
            writer.println("ERROR: No pending challenge.");
            return;
        }

        ClientHandler challenger = ChessServer.getClient(pendingChallenger);

        if (challenger != null) {
            challenger.sendMessage("REJECTED: " + username + " rejected your challenge.");
        }

        writer.println("Challenge rejected.");
        pendingChallenger = null;
    }

    private void runGame() throws IOException {
        writer.println("Welcome " + username + ". You are " + color + ".");
        writer.println("Move format: MOVE A 0 B 0");
        writer.println("Example: MOVE G 4 E 4 moves the white pawn forward two spaces.");
        writer.println("Type QUIT to leave the game.");

        // Show the board once when the game starts
        writer.println(game.getCurrentBoard());

        while (inGame) {

            /*
             * If it is not this player's turn, only print the waiting message once.
             * The loop still checks every second, but it does not spam the console.
             */
            boolean alreadyPrintedWaiting = false;

            while (inGame && game.getCurrentTurn() != color) {
                if (!alreadyPrintedWaiting) {
                    writer.println("Waiting for " + game.getCurrentTurn() + " to move...");
                    alreadyPrintedWaiting = true;
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    writer.println("ERROR: Game interrupted.");
                    return;
                }
            }

            if (!inGame) {
                break;
            }

            writer.println(color + ", it is your turn. Enter your move:");

            String input = reader.readLine();

            if (input == null) {
                throw new IOException("Client disconnected during game.");
            }

            input = input.trim();

            if (input.equalsIgnoreCase("QUIT")) {
                writer.println("VALID: You quit the game.");

                if (opponent != null) {
                    opponent.sendMessage("Opponent quit the game.");
                    opponent.notifyOpponentQuit();
                }

                notifyOpponentQuit();
                break;
            }

            if (!input.toUpperCase().startsWith("MOVE ")) {
                writer.println("INVALID: Use MOVE fromRow fromCol toRow toCol. Example: MOVE G 4 E 4");
                continue;
            }

            String move = input.substring(5);
            String result = game.ProcessMove(move);

            writer.println(result);

            if (result.startsWith("VALID")) {
                game.switchTurn();

                writer.println("Move accepted. Waiting for opponent...");
                writer.println(game.getCurrentBoard());

                if (opponent != null) {
                    opponent.sendMessage("Opponent moved. It should now be your turn.");
                    opponent.sendMessage(game.getCurrentBoard());
                }
            }
        }
    }

    private void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public boolean isInGame() {
        return inGame;
    }

    public String getUsername() {
        return username;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setOpponent(ClientHandler opponent) {
        this.opponent = opponent;
    }

    public void setGameSession(GameSession game) {
        this.game = game;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPendingChallenger(String username) {
        this.pendingChallenger = username;
    }

    public void sendMessage(String msg) {
        if (writer != null) {
            writer.println(msg);
        }
    }

    public synchronized void notifyOpponentQuit() {
        this.inGame = false;

        if (game != null) {
            game.endGame();
        }
    }
}