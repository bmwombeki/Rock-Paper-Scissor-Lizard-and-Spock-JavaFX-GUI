import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.Serializable;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientGui extends Application {
    // the items needed for user Interfaces
    TextField portNum,chatTextField;
    Button serverChoice,clientChoice,b1,playAgain, sendChat, startGame, challenge;
    Button r,p,l,sp, sc;
    HashMap<String, Scene> sceneMap;
    GridPane grid;
    VBox buttonBox;
    VBox clientBox, clientBox3, clientBox4,clientBox5;
    Scene startScene;
    BorderPane startPane;
    Client clientConnection;
    VBox paperButton;
    VBox imageButtons2;
    VBox imageButtons3;
    VBox rockButton;
    VBox lizardButton;
    VBox scissorButton;
    VBox spockButton;
    VBox buttonChoices;
    BorderPane gamePane = new BorderPane();
    String choice ="";
    GameInfo gameInfo = new GameInfo();
    ImageView vrock, vpaper, vlizard, vscissor, vspock;
    TextField IP;
    VBox imageButtons4;
    TextArea clientNum,numberOfClients;
    Button start;
    VBox clientBox2;
    BorderPane clientWait = new BorderPane();
    BorderPane clientPlayAgain = new BorderPane();
    VBox startButtonVBox;
    Button quit, playAgainAfterGameOver;
    VBox quitPlayAgain;
    Label  label1, label2,label3,label4,label5;
    //HashMap<String, ListView> lists = new HashMap<>();
    ListView<String> listItems2, waitList,listView,playersListView,chatListView,clientRequest;
    ObservableList<String> topics;
    boolean alreadySelected = false;



    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Rock Paper Lizard Scissors Spock Client");
        this.portNum = new TextField();
        startGame = new Button("Start Game");
        portNum.setPromptText("Enter port Number");
        portNum.setMaxSize(250,250);
        this.IP = new TextField();
        IP.setPromptText("Enter IP Address");
        IP.setMaxSize(250,250);
        this.clientChoice = new Button("Connect!");
        this.clientChoice.setStyle("-fx-pref-width: 100px");
        this.clientChoice.setStyle("-fx-pref-height: 50px");

        //start game  button event handler
        this.clientChoice.setOnAction(e->{
            primaryStage.setScene(sceneMap.get("clientWaitScreen"));
            clientConnection = new Client(data ->{//defining consumer interface
                Platform.runLater(()->{
                    gameInfo = (GameInfo)data;

                    System.out.println("Requests message(true/false): " + gameInfo.isRequestsMessage);
                    System.out.println("isPlayersList message : " + gameInfo.isPlayersListMessage);
                    System.out.println("Chat message: " + gameInfo.isChatMessage);

                    System.out.println("Client#: " + gameInfo.client);
                    System.out.println("Requests message: " + gameInfo.requestMessage);

                    numberOfClients.clear(); // clear TextArea first
                    numberOfClients.appendText("Total Number of Players  " + gameInfo.clientsList.size());


                    if (gameInfo.isPlayersListMessage) {
                        playersListView.getItems().clear();
                        //loop through the array list of clients
                        for (int i = 0; i < gameInfo.clientsList.size(); i++) {
                            playersListView.getItems().add("Player " + gameInfo.clientsList.get(i).toString());
                        }
                    }

                    if(gameInfo.isRequestsMessage){
                        clientRequest.getItems().add(gameInfo.requestMessage);
                    }

                    if(gameInfo.isChatMessage){
                        chatListView.getItems().add(gameInfo.chatMessage);
                    }

                    if (gameInfo.isThirdSceneMessage){
                        listItems2.getItems().add("The client #" + gameInfo.client + " chose: " + gameInfo.choice);
                    }

                    //check if the flags are set to 1.
                    System.out.println("right before the if conditional for gameFlag");
                    if(clientConnection.gameInfo.gameFlag == 1){
                        System.out.println("gameInfo.gameFlag : " + clientConnection.gameInfo.gameFlag);
                        primaryStage.setScene(sceneMap.get("client"));
                        clientConnection.gameInfo.gameFlag = 0; //resets the flag to zero for next game.
                    }

                });  // runLater takes runnable
            });

            clientConnection.portNum = Integer.parseInt(portNum.getText());
            clientConnection.ipAddress = IP.getText();
            clientConnection.start(); // This is new thread
        });


        this.startButtonVBox = new VBox(30,clientChoice);
        this.startButtonVBox.setPadding(new Insets(80,80,80,200));


        this.buttonBox = new VBox(60, portNum,IP);
        this.buttonBox .setPadding(new Insets(80,80,80,150));
        startPane = new BorderPane();
        startPane.setPadding(new Insets(70));
        startPane.setCenter(buttonBox);
        startPane.setBottom(startButtonVBox);


        startScene = new Scene(startPane, 800,800);

        listItems2 = new ListView<String>();

        //setOnAction each of these and set GameInfo.p1Plays equal to the string
        Image rock = new Image("rock.jpg", 100, 100, true, true);
        vrock = new ImageView(rock);

        Image paper = new Image("paper.jpg",100, 100, true, true);
        vpaper = new ImageView(paper);

        Image scissor = new Image("scissors.jpg",100, 100, true, true);
        vscissor = new ImageView(scissor);

        Image lizard = new Image("lizard.jpg",100, 100, true, true);
        vlizard = new ImageView(lizard);

        Image spock = new Image("spock.jpg",100, 100, true, true);
        vspock = new ImageView(spock);

//**************************************************** EVENT HANDLER *************************************************
        r = new Button();
        r.setOnAction(e->{
            gameInfo.choice = "Rock";
            gameInfo.isThirdSceneMessage = true;
            clientConnection.send(gameInfo);
            r.setDisable(true);						
            p.setDisable(true);						//  disabled all buttons after one click because they can only play once
            l.setDisable(true);					    //  did this to all buttons
            sc.setDisable(true);
            sp.setDisable(true);
        });
        r.setGraphic(vrock);

        p = new Button();
        p.setOnAction(e->{
            gameInfo.choice = "Paper";
            gameInfo.isThirdSceneMessage = true;
            clientConnection.send(gameInfo);
            listItems2.getItems().clear();
            r.setDisable(true);
            p.setDisable(true);
            l.setDisable(true);
            sc.setDisable(true);
            sp.setDisable(true);
        });
        p.setGraphic(vpaper);

        l = new Button("", vlizard);
        l.setOnAction(e->{
            gameInfo.choice = "Lizard";
            gameInfo.isThirdSceneMessage = true;
            clientConnection.send(gameInfo);
            listItems2.getItems().clear();
            r.setDisable(true);
            p.setDisable(true);
            l.setDisable(true);
            sc.setDisable(true);
            sp.setDisable(true);
        });


        sc = new Button();
        sc.setOnAction(e->{
            gameInfo.choice = "Scissor";
            gameInfo.isThirdSceneMessage = true;
            clientConnection.send(gameInfo);
            listItems2.getItems().clear();
            r.setDisable(true);
            p.setDisable(true);
            l.setDisable(true);
            sc.setDisable(true);
            sp.setDisable(true);
        });
        sc.setGraphic(vscissor);

        sp = new Button();
        sp.setOnAction(e->{
            gameInfo.choice = "Spock";
            gameInfo.isThirdSceneMessage = true;
            clientConnection.send(gameInfo);
            listItems2.getItems().clear();
            r.setDisable(true);
            p.setDisable(true);
            l.setDisable(true);
            sc.setDisable(true);
            sp.setDisable(true);
        });
        sp.setGraphic(vspock);

        start = new Button("Start game!");
        this.start.setOnAction(e-> {
            //clientConnection.send(gameInfo);
            primaryStage.setScene(sceneMap.get("client"));
            alreadySelected = false;
        });

        quit = new Button("Exit Game?");
        quit.setOnAction(e -> Platform.exit());

        playAgainAfterGameOver = new Button("Play Again");
        playAgainAfterGameOver.setOnAction(e->{ primaryStage.setScene(sceneMap.get("client"));
            listItems2.getItems().clear();
            r.setDisable(false);
            p.setDisable(false);
            l.setDisable(false);
            sc.setDisable(false);
            sp.setDisable(false);

        });
        chatTextField = new TextField();
        sendChat = new Button("Send Chat");

        challenge = new Button("Click to Challenge");

        // Button to go back to the client second GUI when a client wants to challenge
        challenge .setOnAction(e->{
            primaryStage.setScene(sceneMap.get("clientWaitScreen"));
        });

//***************************************************************************************************
// This gets the text from Chat and sends the message to the server

        //sendChat button event handler
        sendChat.setOnAction(e->{
            gameInfo.chatMessage = chatTextField.getText();
            gameInfo.isChatMessage = true;
            clientConnection.send(gameInfo);
            chatTextField.clear();
        });


        sceneMap = new HashMap<String, Scene>();

        sceneMap.put("client",  createClientGui());
        sceneMap.put("clientWaitScreen", createClientWaitScreenGui());
        sceneMap.put("clientPlayAgainScreen", createClientPlayAgainScreenGui());


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    // This function creates client second scene
    public Scene createClientWaitScreenGui() {

        //ListViews
        playersListView = new ListView<String>();
        numberOfClients = new TextArea();
        chatListView = new ListView<String>();
        clientRequest = new ListView<String>();
        chatTextField.setPromptText("chat...");

        //Labels
        label1 = new Label("List of Players");
        label2 = new Label ("Number Of Players" );
        label3 = new Label (" Players' Chats");
        label4 = new Label ("Player Requests");
        label5 = new Label ("Chat");

        //VBoxes
        clientBox4 = new VBox(10,label1 , playersListView ); // includes players ListView and a label
        // includes number of client Text area, players' chat listView, chat TextField, send chat button , and labels
        clientBox5 = new VBox(10,label2,numberOfClients, label3, chatListView,label5,chatTextField,sendChat);
        clientBox2 = new VBox(10,label4 ,clientRequest, start); // includes player requests and a label
        clientWait.setPadding(new Insets(30,10,40,10)); // spacing nodes on the BorderPane
        clientWait.setRight(clientBox2); // set to the right
        clientWait.setLeft( clientBox4); // set to  the left
        clientWait.setCenter(clientBox5); // set center

//**************************************************************************************************************************
// This makes the list clickable
        playersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        playersListView.getSelectionModel().selectedItemProperty().addListener(
                e->{

                    String selected = playersListView.getSelectionModel().getSelectedItem().toString();

                    gameInfo.isPlaying = true;
                    //clientConnection.gameInfo.gameFlag = 1;
                    gameInfo.isRequestsMessage = true;
                    gameInfo.requestMessage = "Client #" + gameInfo.client + " challenges client #" + selected;
                    gameInfo.opponent = selected;
                    gameInfo.thisPlayer = ("Player " + gameInfo.client);
                    System.out.println(("gameInfo.opponent = " + gameInfo.opponent));
                    System.out.println("gameInfo.thisPlayer = " + gameInfo.thisPlayer);
                    System.out.println("gameInfo.gameFlag == " + clientConnection.gameInfo.gameFlag);

                    alreadySelected = true;
                    clientConnection.send(gameInfo);
                    gameInfo.isRequestsMessage = false;

                });


        return new Scene(clientWait, 800, 800);

    }

    // This function creates client third scene
    public Scene createClientPlayAgainScreenGui() {

        TextArea tA = new TextArea("The game is over!");
        playAgain = new Button("Play Again?");
        listView = new ListView<String>();

        clientBox3 = new VBox(10,tA, listView);
        clientBox3.setStyle("-fx-background-color: blueviolet");
        clientPlayAgain.setCenter(clientBox3);
        clientPlayAgain.setStyle("-fx-background-color: brown");
        return new Scene(clientPlayAgain, 300, 100);

    }


    // This function creates client third scene
    public Scene createClientGui() {

        clientBox = new VBox(10, listItems2);


        paperButton = new VBox(70, p);
        rockButton = new VBox(70, r);
        //imageButtons2 = new VBox(70, l, sc);
        lizardButton = new VBox(70, l);
        scissorButton = new VBox(70, sc);

        //imageButtons3 = new VBox(sp);
        spockButton = new VBox(70, sp);

        buttonChoices = new VBox(25, p, r, l, sc, sp);

        quitPlayAgain = new VBox(20, quit, playAgainAfterGameOver,challenge);

        gamePane.setLeft(clientBox);
        gamePane.setCenter(clientNum);

        gamePane.setCenter(quitPlayAgain);
        gamePane.setRight(buttonChoices);

        return new Scene(gamePane, 500, 500);

    }
}
