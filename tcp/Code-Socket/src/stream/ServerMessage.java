package stream;

import java.io.*;
import java.net.*;


public class ServerMessage extends Thread{
private Socket clientSocket;
	
	ServerMessage(Socket s) {
		this.clientSocket = s;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
		System.out.println("on rentre dans le thread serverMessage");
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		while (true) {
    			System.out.println("echo: " + socIn.readLine());
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
}