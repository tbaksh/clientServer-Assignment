package serverClients;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    //IO Streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;


    @Override
    public void start(Stage primaryStage) throws Exception {

        //Main panel to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a number to see if it's prime: "));


        TextField mainTextField = new TextField();
        mainTextField.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(mainTextField);

        BorderPane mainPane = new BorderPane();
        //Text area to display contents
        TextArea textArea = new TextArea();
        mainPane.setCenter(new ScrollPane(textArea));
        mainPane.setTop(paneForTextField);


        //Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 220);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        mainTextField.setOnAction(event -> {
            try {
                //Get the number from text field
                int userNumber = Integer.parseInt(mainTextField.getText());
                textArea.appendText("Number entered: " + userNumber + "\n");

                //Send number to server
                toServer.writeInt(userNumber);
                toServer.flush();

                //Get verification from server
                int primeServer = fromServer.readInt();

                if (primeServer == 0 ){
                    textArea.appendText(userNumber + " is a prime number \n");
                    textArea.appendText("-------------------------------- \n");
                } else {
                    textArea.appendText(userNumber + " is not a prime number \n");
                    textArea.appendText("-------------------------------- \n");
                }

               // textArea.appendText(String.valueOf(primeServer));
            }
            catch (IOException ex){
                System.err.println(ex + "error 1");
            }
        });

        try {
            //Creates socket to connect to server
            Socket socket = new Socket("localhost", 8000);

            //Create input stream to receive data from server
            fromServer = new DataInputStream(socket.getInputStream());

            //Create output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex){
            textArea.appendText(ex.toString() + "error 2" + '\n');
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}//end of client
