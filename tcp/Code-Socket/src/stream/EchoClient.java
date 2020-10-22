/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;
import java.util.LinkedList;



public class EchoClient {

 
  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
	static volatile boolean connected;
	
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

       /* if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
         // System.exit(1);
        }*/

        try {
      	    // creation socket ==> connexion
      	    echoSocket = new Socket("localhost",1240);
	    socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
	    socOut= new PrintStream(echoSocket.getOutputStream());
	    stdIn = new BufferedReader(new InputStreamReader(System.in));
	    connected=true;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        ServerMessage servermessage= new ServerMessage(echoSocket);
        servermessage.start();
        
       
        while (connected==true) {
        	
        	line=stdIn.readLine();
        	if (line.equals("deconnexion"))
        	{
        		connected=false;// break; //deconnexion doit envoyer un message au serveur , qui va fermer sa socket 
        		//deconnexion : on doit prevenir le thread serverMessage qu'on sest deconnecté
        		//servermessage.stop();
        	}
        	else if(line.equals("creer_groupe"))
        	{
        		System.out.println("à qui voulez vous envoyer le message ? :veuillez entrez les ip (+ports ?) , appuiez sur 1 si vous avez fini");
        		
  		  	}
        	else if(line.equals("envoyer_a_groupe"))
        	{
        		System.out.println("veuillez entrer le numero du groupe : ");//normalement il ya un thread cote server qui des quil recoit "envoyer_a_groupe" , il envoyer les groupes autorisé
        	
        	}
        	socOut.println(line);
        }
      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    }
}

