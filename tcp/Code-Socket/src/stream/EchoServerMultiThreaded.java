package stream;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
	static ConcurrentLinkedQueue<String> history = new ConcurrentLinkedQueue<String>();
	static CopyOnWriteArrayList<ConcurrentLinkedQueue<Socket>> groups = new CopyOnWriteArrayList<ConcurrentLinkedQueue<Socket>>();
	static ConcurrentHashMap<String,Socket> clientsPorts; 
	static ConcurrentHashMap<String,ConcurrentSkipListSet<Integer>> groupsNumber; //la clee est autorisee a parler que au groupes presents dans set
	
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
		groupsNumber = new ConcurrentHashMap<String,ConcurrentSkipListSet<Integer>>();
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			
			ConcurrentSkipListSet<Integer> groupsOfClient = new ConcurrentSkipListSet<Integer>();
			groupsNumber.put(clientSocket.toString(),groupsOfClient);
			
			ClientThread ct = new ClientThread(clientSocket,clientsPorts,history,groups,groupsNumber);
			ct.start();
			
			
			System.out.println("on affiche groups :");
			for(ConcurrentLinkedQueue<Socket> listsocket : groups)
			{
				for(Socket socketdsgroupe : listsocket)
				{
					System.out.print(socketdsgroupe + " ");
				}
				System.out.println();
			}
			System.out.println("on affiche IPGROUP : ");
			for (String key : groupsNumber.keySet()) {
				
				System.out.print(key);
				for(Integer grp : groupsNumber.get(key))
				{
					System.out.print(grp + " ");
				}
				System.out.println();
			}
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServermultithread:" + e);
        }
      }
  }
