
package cw2_pong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author karishma
 */
public class PlayerHandler implements Runnable {
    private Player player;
    private InetAddress roomIP;
    private int roomPort;
    private ServerSocket roomServerSocket;
    private boolean isHost;
    private RoomHandler roomHandler;

    public RoomHandler getRoomHandler() {
        return roomHandler;
    }

    public void setRoomHandler(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }
    
    public boolean isIsHost() {
        return isHost;
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }

    public PlayerHandler(RoomHandler roomHandler, ServerSocket roomServerSocket, int roomPort, InetAddress roomIP) {
        this.roomHandler=roomHandler;
        this.roomServerSocket=roomServerSocket;
        this.roomPort=roomPort;
        this.roomIP=roomIP;
    }


    public Player getPlayer() {
        return player;
    }

    public ServerSocket getRoomServerSocket() {
        return roomServerSocket;
    }
    
    public void setPlayer(Player player){
        this.player=player;
    }

    public InetAddress getRoomIP() {
        return roomIP;
    }

    public void setRoomIP(InetAddress roomIP) {
        this.roomIP = roomIP;
    }

    public int getRoomPort() {
        return roomPort;
    }

    public void setRoomPort(int roomPort) {
        this.roomPort = roomPort;
    }

    private void handle(){
        try{
            Socket connSocket = roomServerSocket.accept();
            ObjectOutputStream objOut = new ObjectOutputStream(connSocket.getOutputStream());
            ObjectInputStream objIn = new ObjectInputStream(connSocket.getInputStream());
            
            isHost = objIn.readBoolean();
            if(isHost){
                double xPos = objIn.readDouble();
                double yPos = objIn.readDouble();
                int score=0;
                InetAddress playerIp=connSocket.getInetAddress();
                int playerPort=connSocket.getPort();
                player = new Player(xPos, yPos, score, playerIp, playerPort );
                System.out.println("Host Player created ");
                System.out.println(player);
                
                //say room that this is player 1
                roomHandler.setPlayer1(player);
            }
            else{
//                //rules, protocols for player 2
                
                double xPos = objIn.readDouble();
                double yPos = objIn.readDouble();
                int score=0;
                InetAddress playerIp=connSocket.getInetAddress();
                int playerPort=connSocket.getPort();
                player = new Player(xPos, yPos, score, playerIp, playerPort );
                System.out.println("Joinee player created");
                System.out.println(player);
                
                roomHandler.setHasGameStarted(true);
                
                //say room that this is player 2
                System.out.println("host is - " + roomHandler.getPlayer1());
                roomHandler.setPlayer2(player);
            
            }
            connSocket.close();
            System.out.println("=========playerHandler end============");
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
        handle();
    }
    
    
    
    
}
