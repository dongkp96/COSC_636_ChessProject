package NetworkLogic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ChessLogic.Color;
import ChessLogic.GameSession;

public class ClientHandler implements Runnable{

    private Socket socket;
    private ClientHandler opponent;
    private GameSession game;
    private String username;
    private Color color;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String move = null;
            String toClient = null;

            writer.println("Please enter a username for you to use (username must contain at " +
                    "least 1 letter or number: ");

            String name = reader.readLine();
            while(name.isBlank()){
                writer.println("Please enter a username for you to use (username must contain at " +
                        "least 1 letter or number: ");
                name = reader.readLine();
            }

            writer.println("VALID: username is set");
            this.setUserName(name);

            writer.println("Welcome " + this.username + " you will be "+ this.color + " in the " +
                    "game");

            while(true){
                game.checkTurn(this.color, writer);
                //Makes the Client Handler wait if it's not their turn
                writer.println(this.game.getCurrentBoard());
                writer.println(this.color + ", it is your turn. Please submit a move: ");
                //sends the message that it is there turn

                do{
                    move = reader.readLine();
                    if(move == null){
                        break;
                    }
                    String[] moveParts = move.split(":");
                    switch(moveParts[0]){
                        case "MOVE":
                            toClient = game.ProcessMove(moveParts[1].stripLeading());
                            writer.println(toClient);
                            break;
                        default:
                            toClient = "INVALID COMMAND";
                            writer.println(toClient);
                    }
                }while(!toClient.startsWith("VALID"));
                //Logic for handling the different commands from the client
                if(move == null){
                    break;
                }
                writer.println(this.game.getCurrentBoard());
                //if move is valid, sends the board state again to the client
                game.switchTurn();
                //switches the turns

            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOpponent(ClientHandler opponent){
        this.opponent = opponent;
    }

    public void setGameSession(GameSession game){
        this.game = game;
    }

    public void setUserName(String name){
        this.username = name;
    }

    public void setColor(Color color){
        this.color = color;
    }
}
