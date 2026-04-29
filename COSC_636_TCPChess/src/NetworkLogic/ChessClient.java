package NetworkLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter server IP address, or press Enter for localhost: ");
        String ipAddress = input.nextLine().trim();

        if (ipAddress.isBlank()) {
            ipAddress = "localhost";
        }

        try {
            Socket socket = new Socket(ipAddress, 5000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            Thread serverReader = new Thread(() -> {
                try {
                    String message;

                    while ((message = reader.readLine()) != null) {
                        System.out.println(message.replace("|", "\n"));
                    }

                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            serverReader.start();

            while (true) {
                String command = input.nextLine();

                writer.println(command);

                if (command.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

            socket.close();

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}