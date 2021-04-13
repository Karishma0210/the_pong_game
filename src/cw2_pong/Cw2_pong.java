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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private static final double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
    private static final double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();
    
    private final int serverPort = 9999;
    private InetAddress serverIP;
    private int roomPort;
    private Socket roomSocket;
    private ObjectOutputStream roomObjOut;
    private ObjectInputStream roomObjIn;
    boolean showPongHostScreen=false;
    boolean isHost;
    
    private static final int PLAYER_HEIGHT = 120;
    private static final int PLAYER_WIDTH = 20;
    private static final double BALL_RADIUS = 14;
    
    private double player1XPos;
    private double player1YPos;
    
    private double player2XPos;
    private double player2YPos;
    
    private double ballXPos;
    private double ballYPos;
    
    private double anchorPaneHeight=SCREEN_HEIGHT*0.7;
    private double anchorPaneWidth=SCREEN_WIDTH*0.7;
    
    Rectangle wallP1;
    Rectangle wallP2;
    Group wallP1Group;
    Group wallP2Group;
    AnchorPane anchorPane;
    Scene scene;
    Runnable showScreenFirstTime;
    //predefining bcz listeners needs to be attached
    Button enterRoomButton= new Button("Enter");
    TextField gameCodeField = new TextField();
    VBox tempMsgHolderOnGame = new VBox();
    VBox hostMsgHolder;
    VBox joineeMsgHolder;
    
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
            
                
            //clear all nodes attached to popUpholder
            popUpHolder.getChildren().clear();

            //bring room code from server
            try(Socket socket = new Socket(serverIP, serverPort)) {
                ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
                isHost=true;
                objOut.writeBoolean(isHost); //letting mainserver know if player is host
                objOut.flush();

                roomPort = objIn.readInt(); //read allocated port from main server

//                System.out.println("closing connection with main server");
                socket.close();
            
                //add pop msg label
//                Label popMsgLabel = new Label();
//                popMsgLabel.setText("Your game code to join is: ");
//                popUpHolder.getChildren().add(popMsgLabel);
//
//                //room code display
//                gameCodeField = new TextField();
//                gameCodeField.setText(Integer.toString(roomPort));
//                gameCodeField.setEditable(false);
//                popUpHolder.getChildren().add(gameCodeField);

//                popUpHolder.setVisible(true); //make it visible
                //disable other buttons
                hostGameButton.setDisable(true);
                joinGameButton.setDisable(true);
                //let the code be on screen for 5 sec

                showPongHostScreen=true;
                showScreenFirstTime.run();
                
            } catch (IOException ex) {
                System.out.println("main server error");
            }
            
        });
        
        joinGameButton.setOnAction((ActionEvent e) -> {
            //clear all nodes attached to popUpholder
            popUpHolder.getChildren().clear();
            
            //add pop msg label
            Label popMsgLabel = new Label();
            popMsgLabel.setText("Enter code to join: ");
            popUpHolder.getChildren().add(popMsgLabel);
            
            gameCodeField = new TextField();
            gameCodeField.setEditable(true);
            popUpHolder.getChildren().add(gameCodeField);
            
//            button already defined, not defining again bcz listner is attached with previous one
            popUpHolder.getChildren().add(enterRoomButton);
            
            popUpHolder.setVisible(true); //make it visible
            
        });
        
        //a separate thread to display screen first time
        showScreenFirstTime = () -> {
            if(showPongHostScreen){
//                System.out.println("it should show pong screen");
                Platform.runLater(()-> { //to attach it with JavaFX thread
                    try {
                        //player 1 body
                        wallP1 = new Rectangle(PLAYER_WIDTH, PLAYER_HEIGHT, Color.GREEN);
                        //                wallP1.setX(0.0);
                        //                wallP1.setY(0.0);
                        //player 1 body container
                        wallP1Group = new Group(wallP1);

                        //player 2 body
                        wallP2 = new Rectangle(PLAYER_WIDTH, PLAYER_HEIGHT, Color.RED);
                        //                wallP2.setX(SCREEN_WIDTH-PLAYER_WIDTH);
                        //                wallP2.setY(0.0);
                        //player 2 body container
                        wallP2Group = new Group(wallP2);

                        //ball body
                        Circle ball = new Circle(BALL_RADIUS, Color.GREEN);
                        ballXPos=anchorPaneWidth/2;
                        ballYPos=anchorPaneHeight/2;
                        ball.setCenterX(ballXPos);
                        ball.setCenterY(ballYPos);

                        //to hold graphics on pane based on X, Y coordinates
                        anchorPane = new AnchorPane();
                        anchorPane.setBackground(
                                new Background(
                                    new BackgroundFill(
                                        Color.AZURE,
                                        CornerRadii.EMPTY,
                                        Insets.EMPTY
                                    )
                                )
                        );

                        //player1 body position
                        player1XPos=0.0;
                        player1YPos=anchorPaneHeight/2;
                        AnchorPane.setLeftAnchor(wallP1Group, player1XPos);
                        AnchorPane.setTopAnchor(wallP1Group, player1YPos);
                        anchorPane.getChildren().add(wallP1Group);

                        //player2 body position yet not defined
                        player2XPos=anchorPaneWidth-PLAYER_WIDTH;
                        player2YPos=anchorPaneHeight/2;
                        AnchorPane.setLeftAnchor(wallP2Group, player2XPos);
                        AnchorPane.setTopAnchor(wallP2Group, player2YPos);
                        anchorPane.getChildren().add(wallP2Group);

                        //ball body position
                        AnchorPane.setTopAnchor(ball, ballYPos);
                        AnchorPane.setLeftAnchor(ball, ballXPos);
                        anchorPane.getChildren().add(ball);
                        
                        
//                        try {
//                            Thread.sleep(4000);
//                        }catch(Exception ex){}

                        //join in the room created
                        roomSocket = new Socket(serverIP, roomPort);
                        roomObjOut = new ObjectOutputStream(roomSocket.getOutputStream());
                        roomObjIn = new ObjectInputStream(roomSocket.getInputStream());
                        //tell room that player is host
                        roomObjOut.writeBoolean(isHost);
                        if(isHost){
                            //send player 1 positions
                            roomObjOut.writeDouble(player1XPos);
                            roomObjOut.writeDouble(player1YPos);
                            roomObjOut.flush();
                            
                            //keep code till other user joins
                            hostMsgHolder = new VBox();
                            Label tempCodeLabel = new Label();
                            tempCodeLabel.setText("Your game code to join is: ");
                            hostMsgHolder.getChildren().add(tempCodeLabel);
                            
                            //temp room code display
                            TextField tempCodeField = new TextField();
                            tempCodeField.setText(Integer.toString(roomPort));
                            tempCodeField.setEditable(false);
                            hostMsgHolder.getChildren().add(tempCodeField);
                            
                            //attach it to game msg holder
                            tempMsgHolderOnGame.getChildren().add(hostMsgHolder);
                        }else{
                            
                            //send player 2 positions
                            roomObjOut.writeDouble(player2XPos);
                            roomObjOut.writeDouble(player2YPos);
                            roomObjOut.flush();
                            
                            //display msg on joinee screen
                            joineeMsgHolder = new VBox();
                            joineeMsgHolder.getChildren().add(
                                new Label("welcome to room " + roomPort)
                            );
                            tempMsgHolderOnGame.getChildren().remove(hostMsgHolder);
                            tempMsgHolderOnGame.getChildren().add(joineeMsgHolder);
                            
                            
                        }
                        roomSocket.close();
                        Platform.runLater(()-> {
                            try {
                                Thread.sleep(2000); //say hello for 2 sec
                            }catch(Exception ex){}

                            anchorPane.getChildren().removeAll(tempMsgHolderOnGame);
                        });
                        
                        //code holder position
                        AnchorPane.setTopAnchor(tempMsgHolderOnGame, anchorPaneWidth*0.10);
                        AnchorPane.setLeftAnchor(tempMsgHolderOnGame, anchorPaneWidth*0.30);
                        anchorPane.getChildren().add(tempMsgHolderOnGame);
                        
                        
                        //attach anchorPane to scene, then make it current window
                        scene = new Scene(anchorPane, anchorPaneWidth, anchorPaneHeight);
                        primaryStage.setScene(scene);
                        primaryStage.setX(SCREEN_WIDTH*0.15);
                        primaryStage.setY(SCREEN_HEIGHT*0.10);
                        
                        Platform.runLater(()-> {
                            try {
                                Thread.sleep(2000);
                            }catch(Exception ex){}

                            anchorPane.getChildren().removeAll(tempMsgHolderOnGame);
                        });
                    } catch (Exception ex) {
                        System.out.println(ex.getClass() + " due to " + ex.getCause());
                        ex.printStackTrace();
                    }
                });
            }
        };
        
        
        // force the field to be numeric only
        gameCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<?extends String> observable,
                    String oldValue, 
                    String newValue) {
                System.out.println("method listener added");
                if (!newValue.matches("\\d*")) {
                    System.out.println("inside if");
                    gameCodeField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        enterRoomButton.setOnAction((ActionEvent e) -> {
            //directly enter in room bcz u already know id
            roomPort = Integer.valueOf(gameCodeField.getText()); //read allocated port from main server
//            
            showPongHostScreen=true;
            showScreenFirstTime.run();
            
        });
        //setting current scene as start window
        scene = new Scene(gridPane, SCREEN_WIDTH*0.3, SCREEN_HEIGHT*0.3);
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