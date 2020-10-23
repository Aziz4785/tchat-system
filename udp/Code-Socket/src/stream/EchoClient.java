/***
 * EchoClient
 * Example of a UDP client 
 * Date: 10/01/20
 * Authors:Kanoun Aziz & Grevaud Paul
 */
package stream;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;



public class EchoClient {

 
  /**
  *  main method
  * le client est dans un groupe avec d'autres clients , mais ses messages passent aussi par le serveur 
  **/
    public static void main(String[] args) throws IOException {

    	//socket udp orpheline pour communication avec server
    	DatagramSocket clientSock = new DatagramSocket();
    	InetAddress serverAddr = InetAddress.getByName("localhost");
    	int serverport =1234;
    	
    	InetAddress groupAddr= null;
    	int groupPort = 0;
    	 MulticastSocket s = null;
    	 BufferedReader stdIn = null;
        BufferedReader socIn = null;

        /*if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
        }*/

        try {
      	 groupAddr = InetAddress.getByName("228.5.6.7"); 
      	    groupPort = 6789;
      	  s = new MulticastSocket(groupPort);  // Join the group 
      	  s.joinGroup(groupAddr);
      	stdIn = new BufferedReader(new InputStreamReader(System.in));
      	
      	//envoie un message au serveur pour recevoir la liste
      	
      	String buf = "jemeconnecte";
      	DatagramPacket packet = new DatagramPacket(buf.getBytes(), buf.length(), serverAddr, serverport); // Send datagram packet to server
      	clientSock.send(packet); 
      			
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        ServerMessage servermessage= new ServerMessage(s);
        ClientHistory clienthistory = new ClientHistory(clientSock,serverAddr,serverport);
        servermessage.start();
        clienthistory.start();
        while (true) {
        	line=stdIn.readLine();
        	String msg = line; 
        	DatagramPacket datagram = new DatagramPacket(msg.getBytes(), msg.length(), groupAddr, groupPort); 
        	if (line.equals(".")) break;
        	s.send(datagram);
        	
        	
        }

      stdIn.close();
    
    }
}


