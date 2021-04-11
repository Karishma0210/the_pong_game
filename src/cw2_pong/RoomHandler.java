
package cw2_pong;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

/**
 *
 * @author karishma
 */
public class RoomHandler implements Runnable, Serializable{
    
    private final int port;
    private Player host;
    private int numberOfPlayers;
    
    public RoomHandler(int port) {
        this.port = port;
        this.numberOfPlayers=0;
    }

    public int getPort() {
        return port;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }
    
    
    public void handleRoom(){
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Room Handler Ready at port - " + this.port);
            
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
                        serverSocket,
                        port,
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
