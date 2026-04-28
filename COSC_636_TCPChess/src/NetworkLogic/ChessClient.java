package NetworkLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);

            System.out.println("Please provide the IP address for the server");
            String ipAddress = input.nextLine();
            Socket socket = new Socket(ipAddress, 9000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // ── 1. USERNAME SETUP ──────────────────────────────────────────
            System.out.println(reader.readLine());
            String username = input.nextLine();
            while (username.isBlank()) {
                System.out.println("Invalid username. Must contain at least 1 letter or number.");
                username = input.nextLine();
            }
            writer.println(username);

            String confirmation = reader.readLine();
            while (!confirmation.startsWith("VALID")) {
                System.out.println(confirmation);
                username = input.nextLine();
                writer.println(username);
                confirmation = reader.readLine();
            }
            System.out.println(confirmation);

            // ── 2. COMMAND MENU ────────────────────────────────────────────
            System.out.println(reader.readLine().replace("|", "\n"));

            // ── 3. LOBBY LOOP ──────────────────────────────────────────────
            // State flags
            boolean waitingForMatch = false; // true after AUTO, WAIT, or PLAY sent
            boolean acceptSent      = false; // true after ACCEPT typed, stops re-prompting

            // Send the first command
            String command = input.nextLine().trim();
            while (command.isBlank()) {
                System.out.println("Command cannot be blank.");
                command = input.nextLine().trim();
            }
            writer.println(command);

            // Determine initial waiting state
            String cmd = command.toUpperCase();
            if (cmd.equals("AUTO") || cmd.equals("WAIT") || cmd.startsWith("PLAY")) {
                waitingForMatch = true;
            }

            while (true) {
                // ── A. Read one line from the server ──────────────────────
                String serverMsg = reader.readLine();
                if (serverMsg == null) {
                    System.out.println("Server disconnected.");
                    socket.close();
                    return;
                }

                // ── B. MATCH_STARTED → exit lobby ─────────────────────────
                if (serverMsg.contains("MATCH_STARTED")) {
                    System.out.println(serverMsg);
                    break;
                }

                // ── C. CHALLENGE arrived ──────────────────────────────────
                if (serverMsg.startsWith("CHALLENGE:")) {
                    if (!acceptSent) {
                        System.out.println(serverMsg);
                        System.out.println("Type ACCEPT or REJECT (or press Enter to wait):");
                    }
                    // Don't send anything — wait for player input below
                }

                // ── D. REJECTED notification ──────────────────────────────
                else if (serverMsg.startsWith("REJECTED:")) {
                    System.out.println(serverMsg);
                    waitingForMatch = false;
                    acceptSent = false;
                    System.out.println("Challenge rejected. Enter a command:");
                }

                // ── E. ERROR response ──────────────────────────────────────
                else if (serverMsg.startsWith("ERROR")) {
                    System.out.println(serverMsg);
                    waitingForMatch = false;
                    acceptSent = false;
                }

                // ── F. Normal response ────────────────────────────────────
                else {
                    System.out.println(serverMsg.replace("|", "\n"));
                }

                // ── G. Decide what to send next ───────────────────────────
                if (waitingForMatch) {
                    // We're waiting — read player input (Enter = CHECK)
                    String pollInput = input.nextLine().trim();

                    if (pollInput.isEmpty()) {
                        // Plain Enter → CHECK
                        writer.println("CHECK");
                    } else {
                        String upperPoll = pollInput.toUpperCase();
                        writer.println(pollInput);

                        if (upperPoll.equals("ACCEPT")) {
                            acceptSent = true;
                            // Don't reset waitingForMatch — keep draining until MATCH_STARTED
                        } else if (upperPoll.equals("REJECT")) {
                            waitingForMatch = false;
                            acceptSent = false;
                            System.out.println("Challenge rejected. Enter a command:");
                        }
                        // Any other input while waiting (e.g. MENU, LIST) just sends it
                    }

                } else {
                    // Not waiting — read a full command from player
                    command = input.nextLine().trim();
                    while (command.isBlank()) {
                        System.out.println("Command cannot be blank.");
                        command = input.nextLine().trim();
                    }
                    writer.println(command);

                    cmd = command.toUpperCase();
                    if (cmd.equals("AUTO") || cmd.equals("WAIT") || cmd.startsWith("PLAY")) {
                        waitingForMatch = true;
                        acceptSent = false;
                    }
                }
            }

            // ── 4. PRE-GAME: welcome + initial board ───────────────────────
            System.out.println(reader.readLine());                          // "Welcome X you will be COLOR"
            System.out.println(reader.readLine().replace("|", "\n"));      // initial board

            // ── 5. GAME LOOP ───────────────────────────────────────────────
            while (true) {

                // A. Read board at start of our turn
                String fromServer = reader.readLine();
                if (fromServer == null) break;
                System.out.println(fromServer.replace("|", "\n"));

                // B. Read "it is your turn" message
                fromServer = reader.readLine();
                if (fromServer == null) break;
                System.out.println(fromServer);

                // C. Get move from player
                System.out.println("Commands: MOVE: <from> <to>  (e.g. MOVE: e2 e4) | QUIT:");
                String move = input.nextLine().trim();
                while (move.isBlank()) {
                    System.out.println("Command cannot be blank.");
                    move = input.nextLine().trim();
                }
                writer.println(move);

                // D. Read confirmation (loop on invalid)
                confirmation = reader.readLine();
                if (confirmation == null) break;
                while (!confirmation.startsWith("VALID")) {
                    System.out.println(confirmation);
                    move = input.nextLine().trim();
                    writer.println(move);
                    confirmation = reader.readLine();
                    if (confirmation == null) break;
                }
                System.out.println(confirmation);

                // E. QUIT check
                if (move.toUpperCase().startsWith("QUIT")) {
                    break;
                }

                // F. Read updated board after our move
                String board = reader.readLine();
                if (board == null) break;
                System.out.println(board.replace("|", "\n"));
            }

            System.out.println("Game over. Thanks for playing!");
            socket.close();

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}