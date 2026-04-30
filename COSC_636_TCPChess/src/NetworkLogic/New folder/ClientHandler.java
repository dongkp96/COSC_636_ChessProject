package NetworkLogic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ChessLogic.Color;
import ChessLogic.GameSession;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable{

    private final Socket socket;
    private ClientHandler opponent;
    private GameSession game;
    private String username;
    private Color color;
    private PrintWriter writer;
    private volatile boolean inGame = false;
    private volatile String pendingChallenger = null;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             writer = new PrintWriter(socket.getOutputStream(), true);
            String move = null;
            String toClient = null;
            String fromClient = null;

            writer.println("Please enter a username for you to use (username must contain at " +
                    "least 1 letter or number: ");

            String name = reader.readLine();
            while(name.isBlank()){
                writer.println("Please enter a username for you to use (username must contain at " +
                        "least 1 letter or number: ");
                name = reader.readLine();
            }

            if (!ChessServer.registerClient(name, this)) {
                writer.println("ERROR: Username already taken");
                socket.close();
                return;
            }else {
                this.setUserName(name);
                writer.println("VALID: "+ this.username +" set as username");
            }
            /*
            * 1. Logic for obtaining username and setting username for player
            * */


            socket.setSoTimeout(10000);
            //2Sets socket timer so that no processes can block forever or gets stuck forever

            //3. GameLobby logic
            writer.println("Commands: AUTO->auto match | WAIT->join waiting list | LIST->view players | PLAY <name>->challenge player");
            //A1. Sends initial command list

            while(!inGame) {
                try{
                    fromClient = reader.readLine();
                }catch(SocketTimeoutException e){
                    if(inGame){
                        break;
                    }
                    continue;
                }
                //A. try/catch for if timeout occurs then it breaks or if successful obtains
                // client input

                if (fromClient == null) return;
                //null check to see if client disconnected

                String[] parts = fromClient.split(" ");
                String command = parts[0].toUpperCase();
                //B. Processes Client input

                switch (command) {

                    case "AUTO":
                        try {
                        ChessServer.enterAutoQueue(this);
                        writer.println("Entered auto queue...");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            writer.println("Error: Failed to enter auto queue");
                        }
                        break;
                        //Logic for autoqueueing
                    case "WAIT":
                        ChessServer.enterWaitingList(this);
                        writer.println("Added to waiting list...");
                        break;
                        //logic for WAIT command

                    case "LIST":
                        writer.println(ChessServer.getWaitingList(this.username));
                        break;
                        //logic for listing available players

                    case "PLAY":
                        if (parts.length < 2) {
                            writer.println("ERROR: specify opponent");
                            break;
                        }

                        boolean success = ChessServer.startMatch(this, parts[1]);
                        if (!success) {
                            writer.println("ERROR: opponent not available");
                        }
                        break;
                    case "MENU":
                        writer.println("Commands: AUTO->auto match | WAIT->join waiting list | LIST->view players | PLAY <name>->challenge player");
                        break;
                        //logic for MENU command to resend MENU
                    case "CHECK":
                        if(inGame){
                            writer.println("MATCH_STARTED: Opponent is " + this.opponent.getUsername() );
                        } else if(pendingChallenger != null){
                                 writer.println("CHALLENGE: " + pendingChallenger + " wants to play. Type ACCEPT or REJECT");
                        }else{
                            writer.println("Still waiting");
                        }
                        break;
                        //logic for when player is in the auto queue or waiting
                    case "ACCEPT":
                        if(pendingChallenger == null){
                            writer.println("No pending challenge");
                            break;
                        }
                         String challengerName = pendingChallenger;
                         pendingChallenger = null;  // ← clear FIRST so CHECK can't re-send it
                        ClientHandler challenger = ChessServer.getClient(challengerName);
                        if(challenger == null){
                            writer.println("ERROR: challenger disconnected");
                            pendingChallenger = null;
                            break;
                        }
                        ChessServer.startGame(this, challenger);
                        pendingChallenger = null;
                        break;

                    case "REJECT":
                        if(pendingChallenger == null){
                            writer.println("No pending challenge");
                            break;
                        }
                        ClientHandler rejectedChallenger = ChessServer.getClient(pendingChallenger);
                        if(rejectedChallenger != null){
                            rejectedChallenger.sendMessage("REJECTED: " + this.username + " rejected your challenge");
                        }
                        writer.println("Challenge rejected");
                        pendingChallenger = null;
                        break;
                    default:
                        writer.println("INVALID COMMAND");
                }
            }
            



            //4.Game logic
            writer.println("Welcome " + this.username + " you will be "+ this.color + " in the " + "game");
            writer.println(this.game.getCurrentBoard());
            //sends welcome message
            socket.setSoTimeout(0);
            //removes timeout so players can just wait for each turn instead being on a timer

            while(inGame){
                game.checkTurn(this.color);
                //A.Makes the Client Handler wait if it's not their turn

                if(!this.inGame){
                    break;
                }
                //A1. If the opponent quits, this gets triggered and ends the game for the
                // handler and Client

                //writer.println(this.game.getCurrentBoard());
                //B. Once it's the players turn then board is printed
                

                writer.println(this.color + ", it is your turn. Please submit a move: ");
                //C. sends the message that it is there turn

                do{
                    System.out.println("[" + this.color + "] waiting for move...");
                    move = reader.readLine();
                     System.out.println("[" + this.color + "] received: " + move);
                    if(move == null){
                        break;
                    }
                   // move = move.toUpperCase();
                   // System.out.println(move);

                   if (move.startsWith("MOVE ")) {
                        move = move.replace("MOVE", "MOVE:");
                        
                    }
                    String[] moveParts = move.split(":");
                    String command = moveParts[0].trim().toUpperCase();
                    String moveArgs = moveParts.length > 1 ? moveParts[1].stripLeading() : "";
                    switch(command){
                        case "MOVE":
                            toClient = game.ProcessMove(moveArgs);
                            System.out.println("[" + this.color + "] ProcessMove result: " + toClient);
                            writer.println(toClient);
                            break;
                        case "QUIT":
                            toClient = "VALID: You have decided to quit the game. Goodbye.";
                            writer.println(toClient);
                            opponent.notifyOpponentQuit();
                            break;
                        default:
                            toClient = "INVALID COMMAND";
                            writer.println(toClient);
                    }
                }while(!toClient.startsWith("VALID"));
                //D. Logic for handling the different commands from the client and checking validity

                if(move == null || move.startsWith("QUIT")){
                    break;
                }
                //E. QUIT logic
                 System.out.println("[" + this.color + "] move valid, sending boards");
                String updatedBoard = this.game.getCurrentBoard();

                
                writer.println(updatedBoard);
                opponent.sendMessage(updatedBoard);

                System.out.println("[" + this.color + "] boards sent, switching turn");
                //F. if move is valid, sends the board state again to the client

                game.switchTurn();
                //E.switches the turns
        System.out.println("[" + this.color + "] turn switched");
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
              ChessServer.removeClient(this);
        try { socket.close(); } catch (IOException ignored) {}
        }
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

    public void sendMessage(String msg) {
        if (writer != null) {
            writer.println(msg);
        }

    }

    public synchronized void notifyOpponentQuit(){
        this.inGame = false;
        if(game !=null){
            game.endGame();
        }
    }

    public void setPendingChallenger(String username){
        this.pendingChallenger = username;
    }
}