
package cw2_pong;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

/**
 *
 * @author karishma
 */
public class RoomHandler implements Runnable, Serializable{
    
    private final int roomPort;
//    private Player host;
    private int numberOfPlayers; //to control players in room
    private Player player1; //host
    private Player player2; //joinee
    private boolean hasGameStarted;
    
    public RoomHandler(int port) {
        this.roomPort = port;
        this.numberOfPlayers=0;
        this.hasGameStarted=false;
    }

    public boolean isHasGameStarted() {
        return hasGameStarted;
    }

    public void setHasGameStarted(boolean hasGameStarted) {
        this.hasGameStarted = hasGameStarted;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    
    

    public int getRoomPort() {
        return roomPort;
    }

//    public Player getHost() {
//        return host;
//    }
//
//    public void setHost(Player host) {
//        this.host = host;
//    }
    
    
    public void handleRoom(){
        try{
            ServerSocket serverSocket = new ServerSocket(roomPort);
            System.out.println("Room Handler Ready at port - " + this.roomPort);
            
            while(numberOfPlayers<2){
//                Socket connSocket = serverSocket.accept();
//                System.out.println("Player has joined!!!" + connSocket.getPort()
//                        + " - " + connSocket.getInetAddress());
//                
//                Player player = new Player();
//                player.setIp(connSocket.getInetAddress());
//                player.setPort(connSocket.getPort());
//                player.setPostion(0,0); //x,y
//                player.setScore(0);
//                if(numberOfPlayers==0){
//                    this.host = player;
//                }
                numberOfPlayers++;
                PlayerHandler playerHandler = new PlayerHandler(
                        this, //roomHandler obj
                        serverSocket,
                        roomPort,
                        serverSocket.getInetAddress()
                );
                
                Thread playerThread = new Thread(playerHandler);
                playerThread.start();
                
            }
            
        }
        catch(IOException ex){
            System.out.println("IO EXCEPTION IN ROOM HANDLER");
            ex.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex.getClass() + " due to " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        handleRoom();
    }
    
}
