
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
    private final ServerSocket ROOM_SERVER_SOCKET;

    public PlayerHandler(ServerSocket roomServerSocket, int roomPort, InetAddress roomIP) {
        
        this.ROOM_SERVER_SOCKET=roomServerSocket;
        this.roomPort=roomPort;
        this.roomIP=roomIP;
    }


    public Player getPlayer() {
        return player;
    }

    public ServerSocket getROOM_SERVER_SOCKET() {
        return ROOM_SERVER_SOCKET;
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
            Socket connSocket = ROOM_SERVER_SOCKET.accept();
            ObjectOutputStream objOut = new ObjectOutputStream(connSocket.getOutputStream());
            ObjectInputStream objIn = new ObjectInputStream(connSocket.getInputStream());
            System.out.println(objIn.readUTF());
            
            connSocket.close();
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
