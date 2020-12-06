package serverClients;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea textArea = new TextArea();

        //Create scene and place in stage
        Scene scene = new Scene(new ScrollPane(textArea), 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread( () -> {
            try {
                //Creating server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(()-> textArea.appendText("Server started at " + new Date()));

                //Listen for connection request
                Socket socket = serverSocket.accept();

                //Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                while (true){

                    int flag = 0;
                    int notPrime = 0, prime = 1;

                    //Receive number from client
                    int numberClient = inputFromClient.readInt();
                    textArea.appendText("\n Number received from client to check: " + numberClient);

                    for(int i = 2; i <= numberClient/2; i++){

                        if(numberClient % i == 0){
                            flag = 1;
                        }

                    }//end for loop
                    if (flag != 1){ //prime
                        outputToClient.writeInt(0);
                    } else { //not prime
                        outputToClient.writeInt(1);
                    }


                } //end of while loop
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }).start();

    }

    public static void main (String[] args){
        launch(args);
    }
}
