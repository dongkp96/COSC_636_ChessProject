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
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            //used to write to the client handler
            Scanner input = new Scanner(System.in);
            //used to handle input from the terminal for the player

        }catch(IOException e){
            System.out.println("Error with connection");
        }

    }

}
