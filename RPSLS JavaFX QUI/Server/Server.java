import sun.java2d.pipe.AATextRenderer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.*;

public class Server  {

    int count = 1;
    public int portNumber;
    String whoWonRound;
    String whoWonGame;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    GameInfo gameInfo = new GameInfo();
    GameInfo player1GameInfo, player2GameInfo;
    String player1, player2;
    String player1Choice, player2Choice = "";
    ArrayList<Integer> clientListinServer = new ArrayList<>();
    int gameRoomNumber = 0;

    public class gameRoomIDs{
        String p1;
        String p2;
    }

    HashMap<Integer, gameRoomIDs> gameRoom = new HashMap<>();
    int thisPlayer;

    Server(Consumer<Serializable> call){	// Consumer functional interface
        // Helps to update the listView
        callback = call;					// Allowing all other threads request the application thread, keeps all the req in a queue
        server = new TheServer(); // This is a new Thread
        server.start();
    }

    public class TheServer extends Thread{


        public void run() {

            try(ServerSocket mysocket = new ServerSocket(portNumber);){
                System.out.println("Server is waiting for a client!");
                System.out.println(portNumber);
                while(true) {
//***************************************************************************************************************************
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    gameInfo.client = count;
                    //System.out.println("gameInfo.client = " + gameInfo.client);
                    clientListinServer.add(count);
                    gameInfo.clientsList = clientListinServer;
                    gameInfo.isPlayersListMessage = true;
//***************************************************************************************************************************

                    //callback.accept("updating clients...");
                    clients.add(c);


                    c.start();
                    Thread.sleep(500);
                    count++;
                    c.updateClients(gameInfo); // I moved the updateClient here because the client thread starts before this

                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }

    class ClientThread extends Thread{

        GameInfo gameInfo = new GameInfo();
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        int client1Points = 0;
        int client2Points = 0;
        int roundNumber = 0;
        String whoWonTheGame;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        //**********************************************************************************************************************************
        // Changes are here
        public void updateClients(Serializable game) {        // Running on its own thread

            String isOpponent = "";
            for (int i = 0; i < clients.size(); i++) {    // Still able to access other threads
                ClientThread t = clients.get(i);        // TheServer and Client are inner classes of server
                try {                                    // Every single instance of client thread class has access

                    System.out.println("Update client");

                    t.gameInfo.gameFlag = 1;
                    t.out.writeObject(game);            // Because they are nested they have access to data members of other classes
                    t.out.reset();
                    //}

                } catch (Exception e) {
                }
            }
        }
//**********************************************************************************************************************************

        public void updateClientText(String message){
            for(int i = 0; i < clients.size(); i++){
                ClientThread p = clients.get(i);
                try{
                    p.out.writeObject(message);
                    p.out.reset();
                }
                catch(Exception e){}
            }
        }

        public void run() {

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            updateClients("New client on server: client #"+count);


//******************************************************************************************************************************************
            // Changes are here
            while(true) {
                try {
                    //callback.accept("Receiving...");
                    gameInfo = (GameInfo)in.readObject();
                    gameInfo.client = count; // In order to always diferentiate the clients ID, i had to set the gameInfo.client to count
                    callback.accept("Client #" + gameInfo.client + " chose " + gameInfo.choice + "."); // This will display on the server

                    //if the arraylist of client threads is greater than or equal to 2
                    if(clients.size() >= 2){
                        gameInfo.have2players = true;
                    }
                    else{
                        gameInfo.have2players = false;
                    }
                    thisPlayer = count;


                    if(gameInfo.gameFlag == 1) {
                        //int player1 = gameInfo.client;
                        //int player2 = gameInfo.opponent;
                        gameRoomIDs newGameRoom = new gameRoomIDs();
                        newGameRoom.p1 = gameInfo.thisPlayer;
                        newGameRoom.p2 = gameInfo.opponent;


                        if (gameRoom.containsValue(newGameRoom.p1) == false && gameRoom.containsValue(newGameRoom.p2) == false) {
                            gameRoomNumber++;
                            gameRoom.put(gameRoomNumber, newGameRoom);

                            //gameRoom.put(count, );
                            System.out.println("Inside gameRoom part");
                        }

                        Set<Integer> m1Key = gameRoom.keySet();
                        for (Integer key : m1Key) {
                            System.out.println("<<<Inside the for each loop for the hashmap>>>");
                            System.out.println("this is client " + thisPlayer);
                            System.out.println("count = " + count);
                            System.out.println("hashmap key = " + key);
                            System.out.println("hashmap value 1: " + (gameRoom.get(key).p1.toString()));
                            System.out.println("hashmap value 2: " + (gameRoom.get(key).p2.toString()));
                        }
                        gameInfo.thisPlayer = ""; // reset the strings back to "" in order to wait for the next game
                        gameInfo.opponent = "";// reset the strings back to "" in order to wait for the next game
                        gameInfo.gameFlag = 0;
                    }
//*******************************************************************************************************************************************

                    updateClients(gameInfo);

                }
//******************************************************************************************************************************************
// Changes are here
                catch(IOException e) // It seems like every time the client disconnect we catch the exception only here
                {                   // So I copied the code from the second catch here too, so that the while loop could break
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    clientListinServer.remove(count); // Here I removed the client who left from the ArrayList of clients' IDs     // This should be changed, it gives an error
                    gameInfo.isPlayersListMessage = true;                                                                       // java.lang.IndexOutOfBoundsException: Index 5 out of bounds for length 3
                    gameInfo.clientsList = clientListinServer;
                    for (int i = 0; i < gameInfo.clientsList.size(); i++) {
                        System.out.println("Player " + gameInfo.clientsList.get(i).toString());
                    }
                    updateClients(gameInfo); // I changed the argument from message to gameInfo
                    clients.remove(this);
                    e.printStackTrace();
                    break;
                }
                catch(ClassNotFoundException e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    e.printStackTrace();
                    gameInfo.isPlayersListMessage = true;
                    clientListinServer.remove(count); // Here I removed the client who left from the ArrayList of clients' IDs
                    gameInfo.clientsList = clientListinServer;
                    updateClients(gameInfo); // I changed the argument from message to gameInfo
                    clients.remove(this);
                    break;
                }
//*******************************************************************************************************************************************
            }
        }//end of run
    }//end of client thread

    public String whoWon(String player1, String player2){
        String whoWonMessage = " ";
        String round = "";

        int p1Choice, p2Choice;


        System.out.println("Player1 played: " + player1);
        System.out.println("Player2 played: " + player2);

        if((player1.equals("") || (player2.equals("")))){
            whoWonMessage = "waiting for other player to pick something...";
        }
        if(player1.equals(player2)){
            whoWonMessage = "draw";
        }

        if((player1.equals("Spock") && player2.equals("Scissor"))  || (player1.equals("Spock") && player2.equals("Rock"))){
            //player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1);
            player1 = "";
            player2 = "";
        }
        if((player1.equals("Scissor") && player2.equals("Lizard"))  || (player1.equals("Scissor") && player2.equals("Paper"))){
            //player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1);
            player1 = "";
            player2 = "";
        }
        if((player1.equals("Paper") && player2.equals("Rock"))  || (player1.equals("Paper") && player2.equals("Spock"))){
            //player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1);
            player1 = "";
            player2 = "";
        }
        if((player1.equals("Rock") && player2.equals("Lizard"))  || (player1.equals("Rock") && player2.equals("Scissor"))){
            //player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1);
            player1 = "";
            player2 = "";
        }
        if((player1.equals("Lizard") && player2.equals("Spock"))  || (player1.equals("Lizard") && player2.equals("Paper"))){
            //player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1);
            player1 = "";
            player2 = "";
        }

        //player 2 win conditions
        if((player2.equals("Spock") && player1.equals("Scissor"))  || (player2.equals("Spock") && player1.equals("Rock"))){
            //player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2);
            player1 = "";
            player2 = "";
        }
        if((player2.equals("Scissor") && player1.equals("Lizard"))  || (player2.equals("Scissor") && player1.equals("Paper"))){
            //player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2);
            player1 = "";
            player2 = "";
        }
        if((player2.equals("Paper") && player1.equals("Rock"))  || (player2.equals("Paper") && player1.equals("Spock"))){
            //player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2);
            player1 = "";
            player2 = "";
        }
        if((player2.equals("Rock") && player1.equals("Lizard"))  || (player2.equals("Rock") && player1.equals("Scissor"))){
            //player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2);
            player1 = "";
            player2 = "";
        }
        if((player2.equals("Lizard") && player1.equals("Spock"))  || (player2.equals("Lizard") && player1.equals("Paper"))){
            //player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2);
            player1 = "";
            player2 = "";
        }
        return whoWonMessage;

    }


}

// Added -> playAgain, isPlaying, gameRoomNum, ArrayList
class GameInfo implements Serializable {
    int gameFlag = 0;
    private static final long serialVersionUID = 1L;
    ArrayList<Integer> clientsList = new ArrayList<Integer>();
    ArrayList<String> clientMessage = new ArrayList<>();
    boolean have2players = false;
    boolean isPlayersListMessage = false;
    boolean isRequestsMessage = false;
    boolean isThirdSceneMessage = false;
    boolean isChatMessage = false;
    String playersMessage = " ";
    String requestMessage = " "; 
    String chatMessage = " ";
    String thirdSceneMessage = " ";
    int client = 0;
    int roundNum = 0;
    int p1RoundNum = 0;
    int p2RoundNum = 0;
    // boolean playAgain; this is the same as newGame
    String choice = "";
    int p1Points = 0;
    int p2Points = 0;
    boolean newGame = false;
    boolean isPlaying = false;
    int points = 0;
    int gameRoomNum = 0;
    String p1Plays = "";
    String p2Plays = "";
    String opponent;
    String thisPlayer;

}
