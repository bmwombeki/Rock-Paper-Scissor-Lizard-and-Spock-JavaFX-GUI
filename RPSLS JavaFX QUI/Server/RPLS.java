import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import static java.lang.Integer.parseInt;

public class RPLS extends Application {

    TextField s1,s2,s3,s4,portNum;
    Button serverChoice, setPort,b1, pn;
    HashMap<String, Scene> sceneMap;
    GridPane grid;
    VBox buttonBox;
    Scene startScene;
    BorderPane startPane;
    Server serverConnection;
    Button exitButton;
    TextArea clientsConnected;
    int numberOfClientsConnected = 0;


    ListView<String> listItems;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Rock Paper Scissors Lizard Spock Server");
        this.portNum = new TextField();
        portNum.setPromptText("Enter port Number");
        this.setPort = new Button("Set port number");
        this.serverChoice = new Button("Turn on Server");
        this.serverChoice.setStyle("-fx-pref-width: 100px");
        this.serverChoice.setStyle("-fx-pref-height: 50px");

        
        // serverChoice event handler
        this.serverChoice.setOnAction(e->{
            primaryStage.setScene(sceneMap.get("server"));
            primaryStage.setTitle("This is the Server");
            serverConnection = new Server(data -> {
                Platform.runLater(()->{
                    listItems.getItems().add(data.toString());
                    numberOfClientsConnected = serverConnection.clients.size();
                });
            });
            serverConnection.portNumber = Integer.parseInt(portNum.getText());
            primaryStage.setTitle("Server port # " + serverConnection.portNumber);
        });

        this.buttonBox = new VBox(10, portNum,serverChoice);
        startPane = new BorderPane();
        startPane.setPadding(new Insets(100));
        startPane.setCenter(buttonBox);

        startScene = new Scene(startPane, 500,500);


        listItems = new ListView<String>();

        sceneMap = new HashMap<String, Scene>();

        sceneMap.put("server",  createServerGui());

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

    // This function creates server scene
    public Scene createServerGui() {

        exitButton = new Button("Exit?");
        exitButton.setOnAction(e->Platform.exit());
        
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(70));
        pane.setStyle("-fx-background-color: coral");

        pane.setCenter(listItems);
        pane.setBottom(exitButton);
        //pane.setBottom(clientsConnected);

        return new Scene(pane, 500, 400);


    }


}
