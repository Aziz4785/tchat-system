package stream;

/***
 * ClientThread
 * Example of a TCP client
 * Date: 10/01/20
 * Authors: Kanoun Aziz & Grevaud Paul
 */
import java.io.*;
import java.net.*;


public class ServerMessage extends Thread{
private Socket clientSocket;
	ServerMessage(Socket s) {
		this.clientSocket = s;
	}

 	//attend un message du serveur , si il le recoit il l'affiche
	/**
	 * le thread ServerMessage va attendre un message en provenance du serveur , une fois recu , il l'affichera dans la console
	 * 
	 */
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		while (true) {
    			String linefromServer = socIn.readLine();
    			System.out.println(linefromServer);
    			if(linefromServer.equals("deconnexion"))
    			{
    				break;
    			}
    		}
    	} catch (Exception e) {
        	System.err.println("Error in servermessage:" + e); 
        }
       }
	
  
}