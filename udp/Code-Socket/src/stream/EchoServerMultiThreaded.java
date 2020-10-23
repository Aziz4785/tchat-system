/***
 * EchoServer
 * Example of a UDP server
 * Date: 10/01/20
 * Authors: Kanoun Aziz & Grevaud Paul
 */

package stream;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 
 * des qu'un client se connecte le serveur lui envoie l'historique de la conversation 
 *
 */
public class EchoServerMultiThreaded  {
  
 	
	 static LinkedList<String> history = new LinkedList<String>();
	 
	 static InetAddress groupAddr= null;
 	static int groupPort = 0;
 	
 	static MulticastSocket s = null;
	 BufferedReader stdIn = null;
   BufferedReader socIn = null;
   /**
 	* main method
	* @param EchoServer port
 	* 
 	**/
       public static void main(String args[]){ 

	try {
		groupAddr = InetAddress.getByName("228.5.6.7"); 
  	    groupPort = 6789;
  	    s = new MulticastSocket(groupPort);  // Join the group 
  	    s.joinGroup(groupAddr);
  	    
  	  int portecoute = 1234;
  	  DatagramSocket serverSockecoute = new DatagramSocket(portecoute);
  	  
  	  //pour la persistance , rajouter ici : on charge dabord laliste a partir dun fichier 
  	  Scanner scan = new Scanner(new File("history.txt"));
  	  while (scan.hasNextLine()){
  	    history.add(scan.nextLine());
  	  }
  	  scan.close();
  	  
		while (true) {
			  ClientThread ct = new ClientThread(serverSockecoute,history);
	   		  ct.start();
			 byte[] buf = new byte[256];//Create a datagram packet
   		  DatagramPacket recv = new DatagramPacket(buf, buf.length); // Wait for a packet
   		  s.receive(recv);
   		  //on recupere le message du packet dans str
   		  String str = new String(
    		    recv.getData(),
    		    recv.getOffset(),
    		    recv.getLength(),
    		    StandardCharsets.UTF_8 // or some other charset
    		);
   		  history.add(recv.getSocketAddress()+" : "+str);
   		  
   		//pour la persistence : on ecrit le contenu de la liste dans un fichier
   		FileWriter writer = new FileWriter("history.txt",true); // le true signifie que append=true  
   		writer.write(recv.getSocketAddress()+" : "+str + System.lineSeparator());
   		writer.close();
   		
		}
		
		
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }

  

  
