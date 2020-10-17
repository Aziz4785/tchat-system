/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	ConcurrentHashMap<String,Socket> clientsPorts;
	
	ClientThread(Socket s,ConcurrentHashMap<String,Socket> clientsPorts) {
		this.clientSocket = s;
		this.clientsPorts=clientsPorts;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
		System.out.println("on appelle le thread");
    	  try {
    		  System.out.println("on stocke la socket dans la hashmap");
    		  System.out.println(clientSocket.toString()+" -> "+clientSocket);
    		  clientsPorts.put(clientSocket.toString(),clientSocket);
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		//PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		while (true) {
    		  String line = socIn.readLine();
    		  //socOut.println(line);
    		  for(Socket socket : clientsPorts.values())
    		  {
    			  PrintStream socOut = new PrintStream(socket.getOutputStream());
    			  socOut.println(line);
    		  }
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
  }
