package NetworkLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {
    public static void main(String[] args) {
        try{
            Scanner input = new Scanner(System.in);
            //used to handle input from the terminal for the player
            System.out.println("Please provide the IP address for the server");
            String ipAddress = input.nextLine();
            Socket socket = new Socket("localhost", 50);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //to read messages from the Client Handler
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            //used to write to the client handler

            System.out.println(reader.readLine());
            //prints the message to do your username
            String username = input.nextLine();

            while(username.isBlank()){
                System.out.println("Invalid username. Username must contain at least 1 letter or " +
                        "number)");
                username = input.nextLine();
            }
            //Ensures client side entry with username is correct

            writer.println(username);
            //sends username to client handler to check

            String confirmation = reader.readLine();
            while(!confirmation.startsWith("VALID")){
                System.out.println(confirmation);
                username = input.nextLine();
                writer.println(username);
                confirmation = reader.readLine();
            }
            //works with invalid username until its valid

            System.out.println(reader.readLine());
            //Used to read welcome message from the Client handler

            String move = null;
            // initializes the String move, that will be the move holder
            String board = null;
            while(true){
                System.out.println(reader.readLine().replace("|", "\n"));
                //reads the board being sent to the player
                System.out.println(reader.readLine());
                //reads the "it is your turn message"
                System.out.println("Commands that can be entered: ");
                System.out.println("Move = MOVE: followed by the desired coordinates such as e2 " +
                        "e4");
                move = input.nextLine();
                while(move.isBlank()){
                    System.out.println("You did not enter a command. Please enter a command ");
                    move = input.nextLine();
                }
                //gets input for the move and ensures move is not blank
                writer.println(move);
                //sends the move
                confirmation = reader.readLine();
                //obtains the confirmation of the move if it's valid or not
                while(!confirmation.startsWith("VALID")){
                    move = input.nextLine();
                    writer.println(move);
                    confirmation = reader.readLine();
                }
                //logic for an invalid move

                board = reader.readLine();
                System.out.println(board.replace("|", "\n"));
                //prints the board out to the player


            }


        }catch(IOException e){
            System.out.println("Error with connection");
        }

    }

}
