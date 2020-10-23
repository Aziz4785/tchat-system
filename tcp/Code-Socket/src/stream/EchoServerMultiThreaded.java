package stream;

/***
 * EchoServerMultiThreade
 * Example of a TCP server
 * Date: 10/01/20
 * Authors: Kanoun Aziz & Grevaud Paul
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * 
 * attend la connexion d'un client , une fois fait , le client est traité par le thread ClientThread
 */
public class EchoServerMultiThreaded  {
  
 	/**
 	 * history est une liste partagée entre tous les threads , chaque message recu va etre stocké dans history
 	 */
	static ConcurrentLinkedQueue<String> history = new ConcurrentLinkedQueue<String>();
	/**
	 * la liste de socket à l'indice 0 correspond a tous les sockets connectées au serveur , ensuite chaque liste correspond a un groupe de sockets qui peuvent se parler entre elles 
	 */
	static CopyOnWriteArrayList<ConcurrentLinkedQueue<Socket>> groups = new CopyOnWriteArrayList<ConcurrentLinkedQueue<Socket>>();
	/**
	 * chaque clée est le string correspondant à la socket associée (presente dans valeur)
	 */
	static ConcurrentHashMap<String,Socket> clientsPorts; 
	/**
	 * chaque clée correspond à chaque socket du serveur , la valeur est une liste qui contient tous les numeros de groupes à qui appartient la clée 
	 */
	static ConcurrentHashMap<String,ConcurrentSkipListSet<Integer>> groupsNumber; //la clee est autorisee a parler que au groupes presents dans set
	
	
	/**
  	* main method
	* @param EchoServer port
  	* attend la connexion d'un client , une fois fait , le client est traité par le thread ClientThread
  	*/
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
