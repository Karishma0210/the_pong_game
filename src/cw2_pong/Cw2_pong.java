/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw2_pong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author karishma
 */
public class Cw2_pong extends Application {
    //set game screen as 70% of User screen
    private static final double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth()*0.3;
    private static final double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight()*0.4;
    
    private final int serverPort = 9999;
    private InetAddress serverIP;
    private int roomPort;
    Socket roomSocket;
    
    private static final int PLAYER_HEIGHT = 120;
    private static final int PLAYER_WIDTH = 20;
    private static final double BALL_RADIUS = 14;
    
    private double player1XPos = 0;
    private double player1YPos = SCREEN_HEIGHT/2;
    
    private double player2Xpos = SCREEN_WIDTH - PLAYER_WIDTH;
    private double player2YPos = SCREEN_HEIGHT/2;
    
    private double ballXPos = SCREEN_WIDTH/2;
    private double ballYPos = SCREEN_HEIGHT/2;
    
    Rectangle wallP1;
    Rectangle wallP2;
    Group wallP1Group;
    Group wallP2Group;
    AnchorPane anchorPane;
    Scene scene;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            serverIP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("Local Host not found");
        }
        //Content holder Layout for first screen
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        //host game button holder
        HBox hostGameHolder = new HBox();
        //add host game button
        Button hostGameButton = new Button("Host Game");
        hostGameHolder.getChildren().add(hostGameButton);
        hostGameHolder.setAlignment(Pos.CENTER);
        gridPane.add(hostGameHolder, 0, 0); //Col, Row

        //Join game button holder
        HBox joinGameHolder = new HBox();
        //add join game button
        Button joinGameButton = new Button("Join Game");
        joinGameHolder.getChildren().add(joinGameButton);
        joinGameHolder.setAlignment(Pos.CENTER);
        gridPane.add(joinGameHolder, 0, 1); //Col, Row
        
        //pop up holder for any msgs received from server
        VBox popUpHolder = new VBox();
        
        popUpHolder.setAlignment(Pos.CENTER);
        gridPane.add(popUpHolder, 0, 2); //Col, Row
        //initially hide any msgs
        popUpHolder.setVisible(false);
        
        
        
        hostGameButton.setOnAction((ActionEvent e) -> {
            try {
                //clear all nodes attached to popUpholder
                popUpHolder.getChildren().clear();
                
                //bring room code from server
                try(Socket socket = new Socket(serverIP, serverPort)) {
                    ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
                    objOut.writeBoolean(true); //letting mainserver know if player is host
                    objOut.flush();
                    
                    roomPort = objIn.readInt(); //read allocated port from main server
                    
                    
                    System.out.println("closing connection with main server");
                    socket.close();
                }
                
                
                //add pop msg label
                Label popMsgLabel = new Label();
                popMsgLabel.setText("Your game code to join is: ");
                popUpHolder.getChildren().add(popMsgLabel);
                
                //room code display
                TextField gameCodeField = new TextField();
                gameCodeField.setText(Integer.toString(roomPort));
                gameCodeField.setEditable(false);
                popUpHolder.getChildren().add(gameCodeField);
                
                popUpHolder.setVisible(true); //make it visible
                //disable other buttons
                hostGameButton.setDisable(true);
                joinGameButton.setDisable(true);
                System.out.println("before sleep, showing pop up" );
                //let the code be on screen for 5 sec
                
                Runnable mtd = () -> {
                    try {
                        //player 1 body
                        wallP1 = new Rectangle(PLAYER_WIDTH, PLAYER_HEIGHT, Color.GREEN);
                        //                wallP1.setX(0.0);
                        //                wallP1.setY(0.0);
                        //player 1 body container
                        wallP1Group = new Group(wallP1);

                        //player 2 body
                        wallP2 = new Rectangle(PLAYER_WIDTH, PLAYER_HEIGHT, Color.GREEN);
                        //                wallP2.setX(SCREEN_WIDTH-PLAYER_WIDTH);
                        //                wallP2.setY(0.0);
                        //player 2 body container
                        wallP2Group = new Group(wallP2);

                        //ball body
                        Circle ball = new Circle(BALL_RADIUS, Color.GREEN);
                        ball.setCenterX(0.0);
                        ball.setCenterY(0.0);

                        //to hold graphics on pane based on X, Y coordinates
                        anchorPane = new AnchorPane();
                        anchorPane.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                Color.AZURE,
                                                CornerRadii.EMPTY,
                                                new Insets(10))
                                )
                        );
                        //player1 body position
                        AnchorPane.setTopAnchor(wallP1Group, player1YPos);
                        AnchorPane.setLeftAnchor(wallP1Group, 0.0);
                        anchorPane.getChildren().add(wallP1Group);
                        //player2 body position
                        AnchorPane.setLeftAnchor(wallP2Group, 0.0);
                        AnchorPane.setTopAnchor(wallP2Group, player2YPos);
                        anchorPane.getChildren().add(wallP2Group);
                        //ball body position
                        AnchorPane.setTopAnchor(ball, ballYPos);
                        AnchorPane.setLeftAnchor(ball, ballXPos);
                        anchorPane.getChildren().add(ball);


                        roomSocket = new Socket(serverIP, roomPort);
                        ObjectOutputStream objOut = new ObjectOutputStream(roomSocket.getOutputStream());
                        ObjectInputStream objIn = new ObjectInputStream(roomSocket.getInputStream());
                        objOut.writeUTF("intending inside player");
                        objOut.flush();



                        try {
                            sleep(5000);
                        } catch (InterruptedException ex) {
                            System.out.println("sleep exception, may be awaken");
                        }
                        //attach anchorPane to scene, then make it current window
                        scene = new Scene(anchorPane, SCREEN_WIDTH, SCREEN_HEIGHT);
                        primaryStage.setScene(scene);
                    } catch (IOException ex) {
                        Logger.getLogger(Cw2_pong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };
                mtd.run();
                
            } catch (IOException ex) {
                System.out.println("IOException on client");
            }catch (Exception ex) {
                System.out.println("Due to sleep() there is an error");
            }
        });
        
        joinGameButton.setOnAction((ActionEvent e) -> {
            //clear all nodes attached to popUpholder
            popUpHolder.getChildren().clear();
            
            //add pop msg label
            Label popMsgLabel = new Label();
            popMsgLabel.setText("Enter code to join: ");
            popUpHolder.getChildren().add(popMsgLabel);
            
            TextField gameCodeField = new TextField();
            gameCodeField.setEditable(true);
            popUpHolder.getChildren().add(gameCodeField);
            
            popUpHolder.setVisible(true); //make it visible
            
        });
        //setting current scene as start window
        scene = new Scene(gridPane, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("The Pong Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
   
}

class MyThread extends Thread {
    public void run(){
       System.out.println("MyThread running");
    }
}