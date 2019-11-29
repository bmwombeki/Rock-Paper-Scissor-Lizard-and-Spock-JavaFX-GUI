import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;



public class Client extends Thread{

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    GameInfo gameInfo = new GameInfo();
    int portNum;
    String ipAddress;
    boolean newGame;

    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call){

        callback = call;
    }

    public void run() {

        try {
            socketClient= new Socket(ipAddress,portNum);
            System.out.println(ipAddress);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

//********************************************************************************************************************************
// Changes are here
        while(true) {
            try {
                // send "Receiving..." we are trying to cast String to GameInfo
                gameInfo = (GameInfo)in.readObject();       // which is probably not possible
                callback.accept(gameInfo);

                System.out.println("server - accept");


//********************************************************************************************************************************
            }
            catch(Exception e) {}
        }
    }

    public void send(Serializable data) { //Grab the string, go to output stream, send it

        try {
            out.writeObject(data);
            out.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendText(String data){
        try{
            out.writeObject(data);
        } catch (IOException e){
            e.printStackTrace();
        }
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
    String requestMessage = " "; // I'm not sure if this is the same as playersMessage
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
