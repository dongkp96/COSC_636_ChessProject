package NetworkLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {
    public static void main() {
        try{
            Scanner input = new Scanner(System.in);
            //used to handle input from the terminal for the player
            System.out.println("Please provide the IP address for the server");
            String ipAddress = input.nextLine();
            Socket socket = new Socket(ipAddress, 50);
            //Establishes IP address for Socket
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //to read messages from the Client Handler
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            //used to write to the client handler

            System.out.println(reader.readLine());
            //1. prints the message to do your username

            String username = input.nextLine();
            //2.takes scanner input for username

            while(username.isBlank()){
                System.out.println("Invalid username. Username must contain at least 1 letter or " +
                        "number)");
                username = input.nextLine();
            }
            //Ensures client side entry with username is correct

            writer.println(username);
            //3.sends username to client handler to check

            String confirmation = reader.readLine();
            while(!confirmation.startsWith("VALID")){
                System.out.println(confirmation);
                username = input.nextLine();
                writer.println(username);
                confirmation = reader.readLine();
            }
            //works with invalid username until its valid

            System.out.println(confirmation);
            //4.used to read username set message


            System.out.println(reader.readLine().replace("|", "\n"));
            //5. reads initial command menu message

            String command = input.nextLine();
            while(command.isBlank()){
                System.out.println("Command cannot be left blank. Please input a valid " +
                        "command as listed before");
                command = input.nextLine();
            }
            writer.println(command);
            //6.deals with first command after initial command menu message

            String commandResponse;

            //7. Game lobby loop
            while(true){
                commandResponse = reader.readLine();
                //A. reads the command response
                if(commandResponse.contains("MATCH_STARTED")){
                    System.out.println(commandResponse);
                    break;
                    //A1: addresses if MATCH_STARTED condition is met, so loop can move onto game loop
                }

                if(command.contains("MENU")){
                    System.out.println(commandResponse.replace("|", "\n"));
                }else{
                    System.out.println(commandResponse);
                }
                //A2: reads the command menu if command was sent to ask for menu

                if(command.contains("AUTO") || command.contains("WAIT") || command.contains("PLAY")){
                    System.out.println("Press Enter to check if match has been found...");
                    while(true){
                        input.nextLine();
                        writer.println("CHECK");
                        commandResponse = reader.readLine();
                        if(commandResponse.contains("MATCH_STARTED")){
                            System.out.println(commandResponse);
                            break;
                        }
                        System.out.println(commandResponse);
                    }
                    break;
                    /*A3. Used to check if previous command was auto or wait, block
                    adjusts for the WAIT or AUTO process for matchmaking, so no incorrect
                    reads are done
                    */

                }

                command = input.nextLine().toUpperCase();
                while(command.isBlank()){
                    System.out.println("Command cannot be left blank. Please input a valid " +
                            "command as listed before");
                    command = input.nextLine();
                }
                writer.println(command);
                //B. Takes command from client and sends command to handler

                commandResponse = reader.readLine();
                if(commandResponse.contains("MATCH_STARTED")){
                    System.out.println(commandResponse);
                    break;
                }
                System.out.println(commandResponse);
                //C. Receives and reads response from the command

                if(command.contains("PLAY")){
                    System.out.println("Press Enter to check if opponent has accepted...");
                    while(true){
                        input.nextLine();
                        writer.println("CHECK");
                        commandResponse = reader.readLine();
                        if(commandResponse.contains("MATCH_STARTED")){
                            System.out.println(commandResponse);
                            break;
                        }
                        System.out.println(commandResponse);
                    }
                    break;
                }

            }

            System.out.println(reader.readLine());
            //Used to read welcome to game message from ClientHandler


            String move = null;
            String board = null;
            String fromClient = null;
            // initializes the String move, board, and fromClient, that will be the move holder

            //8. Game loop (null breaks used to check if socket is still connected to handler
            while(true){
                fromClient = reader.readLine();
                if(fromClient == null){
                    break;
                }
                System.out.println(fromClient.replace("|", "\n"));
                //A.reads the board being sent to the player

                fromClient = reader.readLine();
                if(fromClient == null){
                    break;
                }
                System.out.println(fromClient);
                //B.reads the "it is your turn message"

                System.out.println("Commands that can be entered: ");
                System.out.println("Move = MOVE: followed by the desired coordinates such as e2 " +
                        "e4");
                move = input.nextLine();
                while(move.isBlank()){
                    System.out.println("You did not enter a command. Please enter a command ");
                    move = input.nextLine();
                }
                writer.println(move);
                //C. gets input for the move and ensures move is not blank, then sends move


                confirmation = reader.readLine();
                if(confirmation == null){
                    break;
                }
                while(!confirmation.startsWith("VALID")){
                    move = input.nextLine();
                    writer.println(move);
                    confirmation = reader.readLine();
                }
                //D.obtains the confirmation of the move if it's valid or not

                if(move.startsWith("QUIT:")){
                    System.out.println(confirmation);
                    break;
                }

                board = reader.readLine();
                if(board == null){
                    break;
                }
                System.out.println(board.replace("|", "\n"));
                //E.prints the board out to the player


            }

            System.out.println("Client has exited");
            socket.close();
            //9. Socket is closed

        }catch(IOException e){
            System.out.println(e);
        }

    }

}
