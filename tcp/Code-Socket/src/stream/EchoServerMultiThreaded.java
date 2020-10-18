package stream;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
	static ConcurrentLinkedQueue<String> history = new ConcurrentLinkedQueue<String>();
	
	static ConcurrentHashMap<String,Socket> clientsPorts; 
	
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        
  	/*if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
         // System.exit(1);
  	}*/
	try {
		listenSocket = new ServerSocket(1240); //port
		System.out.println("Server ready..."); 
		clientsPorts = new ConcurrentHashMap<String,Socket>(); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			ClientThread ct = new ClientThread(clientSocket,clientsPorts,history);
			ct.start();
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServermultithread:" + e);
        }
      }
  }
