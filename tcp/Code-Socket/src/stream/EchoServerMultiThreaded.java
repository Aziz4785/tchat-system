package stream;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
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
			ClientThread ct = new ClientThread(clientSocket,clientsPorts);
			ct.start();
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }