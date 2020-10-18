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
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	ConcurrentHashMap<String,Socket> clientsPorts;
	ConcurrentLinkedQueue<String> history;
	
	
	ClientThread(Socket s,ConcurrentHashMap<String,Socket> clientsPorts,ConcurrentLinkedQueue<String> history) {
		this.clientSocket = s;
		this.clientsPorts=clientsPorts;
		this.history=history;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		  System.out.println("on stocke la socket dans la hashmap");
    		  System.out.println(clientSocket.toString()+" -> "+clientSocket);
    		  clientsPorts.put(clientSocket.toString(),clientSocket);
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		//PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		
    		//hisotrique : on envoie l'historique au client
    		PrintStream clientsocOut = new PrintStream(clientSocket.getOutputStream());
    		for(String hstymsg : history)
    			clientsocOut.println(hstymsg);
    		
    		while (true) {
    		  String line = socIn.readLine(); //on recupere le message du client
    		  
    		  //deconnexion:  si cest un message de deconnexion , on ferme la socket dans la hashmap et on la supprime de la hashmap
    		  if(line.equals("deconnexion"))
    		  {
    			  System.out.println("le client "+ clientSocket +" veut se deconnecter");
    			  clientsocOut.println("deconnexion"); // on previent le thread qui attend le serveur que le client sest deconnecter
    			  disconnect(clientSocket);
    			  break;
    		  }
    		  else
    		  {
    			//historique : on stocke le messge (expediteur + contenu)
        		  history.add(clientSocket.getInetAddress()+"-"+clientSocket.getLocalPort()+" : "+line);
	    		  
	    		  System.out.println("on va envoyer un message à tous les autres clients : ");
	    		  for(Socket socket : clientsPorts.values()) //on envoie le message à tous les autres clients 
	    		  {
	    			  System.out.println(" -"+socket);
	    			  PrintStream socOut = new PrintStream(socket.getOutputStream());
	    			  socOut.println(socket.getInetAddress()+"-"+socket.getLocalPort()+" : "+line);
	    		  }
    		  }
    		  
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }

	private void disconnect(Socket clientSocket2) throws IOException {
		System.out.println("on va appeler la fonction close()");
		//clientSocket2.getOutputStream().close();
		clientSocket2.close();
		System.out.println("on eleve la socket de la map");
		clientsPorts.remove(clientSocket2.toString());
		System.out.println("la socket "+ clientSocket2 +" est a present fermee");
	}
  
  }
