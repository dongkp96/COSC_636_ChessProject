package NetworkLogic;

import ChessLogic.ChessBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost", 50);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //to read messages from the Client Handler
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            //used to write to the client handler
            Scanner input = new Scanner(System.in);
            //used to handle input from the terminal for the player
            String move = null;
            // initializes the String move, that will be the move holder

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


            while(true){
                //loop for client side terminal commands
            }


        }catch(IOException e){
            System.out.println("Error with connection");
        }

    }

}
