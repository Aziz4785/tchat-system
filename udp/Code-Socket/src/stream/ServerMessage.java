/***
 * ServerMessage
 * Example of a UDP client thread
 * Date: 10/01/20
 * Authors:Kanoun Aziz & Grevaud Paul
 */
package stream;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * des que le client recoit un message du serveur , il l'affiche
 * 
 *
 */
public class ServerMessage extends Thread {
	private MulticastSocket clientmulticastSocket;
	
	//des quil recoit un message du serveur il laffiche
	ServerMessage(MulticastSocket s) {
		this.clientmulticastSocket = s;
	}
	
	public void run() {
		while(true) {
			byte[] buf = new byte[1000]; 
		DatagramPacket recv = new  DatagramPacket(buf, buf.length);
		try {
			clientmulticastSocket.receive(recv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	String str = new String(
    		    recv.getData(),
    		    recv.getOffset(),
    		    recv.getLength(),
    		    StandardCharsets.UTF_8 // or some other charset
    		);
    	
    	SocketAddress clientAddr = recv.getSocketAddress();
	    
    	System.out.println(clientAddr +" : "+str);
		}
     }
}
