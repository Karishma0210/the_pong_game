
package cw2_pong;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author karishma
 */
public class ServerRunner {

    /**
     * @param args the command line arguments
     */
    
    private static final int serverPort = 9999;
    public static void main(String[] args) throws IOException {
        try{
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server Ready!!!");
            
            while(true){
                Socket connSocket = serverSocket.accept();
                System.out.println("Server-client joined!!!" + connSocket.getPort()
                        + " - " + connSocket.getInetAddress());
                ObjectOutputStream objOut = new ObjectOutputStream(connSocket.getOutputStream());
                ObjectInputStream objIn = new ObjectInputStream(connSocket.getInputStream());
                
                //first read whether he is host or joinee
                boolean isHost;
                isHost = objIn.readBoolean();
                if(isHost){
                    int roomPort = getAvailablePort();
                    RoomHandler roomHandler = new RoomHandler(roomPort);
                    Thread roomThread = new Thread(roomHandler);
                    roomThread.start();
                    objOut.writeInt(roomPort); //room handler port
                    objOut.flush();
                    System.out.println("===================");
                    connSocket.close();
                }
            }
        }catch(IOException ex){
            System.out.println("IO EXCEPTION IN SERVER");
            ex.printStackTrace();
        } catch(Exception ex){
            System.out.println(ex.getClass() + " due to " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    private static int getAvailablePort() throws IOException {
        int availablePort = serverPort+1;
        boolean isAllocated=false;
        ServerSocket dummySocket;
        while(!isAllocated){
            availablePort++;
            try{
                dummySocket = new ServerSocket(availablePort);
                isAllocated=true;
                dummySocket.close();
            }catch(IOException ex){
                //genuine availablity check
            }catch(Exception ex){
                System.out.println(ex.getClass() + " due to " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return availablePort;
    }
}
