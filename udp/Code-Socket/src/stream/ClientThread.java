/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class ClientThread
	extends Thread {
	
	private DatagramSocket serverSock;
	private LinkedList<String> history;
	
	ClientThread(DatagramSocket serverSock,LinkedList<String> history) {
		this.serverSock = serverSock;
		this.history=history;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		while (true) {
    			byte[] buf = new byte[256];
    			DatagramPacket newClientPacket = new DatagramPacket(buf, buf.length);
    			serverSock.receive(newClientPacket);
    			
    			 String msgunicast = new String(
    					 newClientPacket.getData(),
    					 newClientPacket.getOffset(),
    					 newClientPacket.getLength(),
    		    		    StandardCharsets.UTF_8 // or some other charset
    		    		);
    			 System.out.println("on a recu le message : "+msgunicast);
    			
    			//serialiser la list
    			ByteArrayOutputStream out = new ByteArrayOutputStream();
    		    ObjectOutputStream outputStream = new ObjectOutputStream(out);
    		    outputStream.writeObject(history);
    		    outputStream.close();
    		    
    		    InetAddress clientAddr = newClientPacket.getAddress(); 
    		    int clientPort = newClientPacket.getPort();
    		    byte[] listData = out.toByteArray();
    		    DatagramPacket historypacket = new DatagramPacket(listData, listData.length, clientAddr, clientPort); 
    		    
    		    System.out.println("-------------on envoie à : "+clientAddr +"-"+clientPort);
    	   		for(String str1 : history)
    		    {
    		    	System.out.println(str1);
    		    }
    	   		System.out.println("-------------");
    		    serverSock.send(historypacket);
    		    
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
  }

  
