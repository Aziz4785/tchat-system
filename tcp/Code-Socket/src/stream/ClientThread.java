/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	ConcurrentHashMap<String,Socket> clientsPorts;
	ConcurrentLinkedQueue<String> history;
	CopyOnWriteArrayList<ConcurrentLinkedQueue<Socket>> groups;
	ConcurrentHashMap<String,ConcurrentSkipListSet<Integer>> groupsNumber;
	int groupeChoisi;
	
	//pour les methodes
	ConcurrentLinkedQueue<Socket> list_destinataire = new ConcurrentLinkedQueue<Socket>();
	
	
	ClientThread(Socket s,ConcurrentHashMap<String,Socket> clientsPorts,ConcurrentLinkedQueue<String> history,CopyOnWriteArrayList<ConcurrentLinkedQueue<Socket>> groups
			,ConcurrentHashMap<String,ConcurrentSkipListSet<Integer>> groupsNumber) {
		this.clientSocket = s;
		this.clientsPorts=clientsPorts;
		this.history=history;
		this.groups=groups;
		this.groupsNumber = groupsNumber;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		  groupeChoisi = 0;
    		  System.out.println("on stocke la socket dans la hashmap");
    		  System.out.println(clientSocket.toString()+" -> "+clientSocket);
    		  
    		  clientsPorts.put(clientSocket.toString(),clientSocket);
    		  if(!groups.isEmpty())
    			  groups.get(0).add(clientSocket);
    		  else
    		  {
    			  ConcurrentLinkedQueue<Socket> broadcastList=new ConcurrentLinkedQueue<Socket>();
    			  broadcastList.add(clientSocket);
    			  groups.add(broadcastList);
    		  }
    		  
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
    		 
    		  else if(line.equals("creer_groupe"))
    		  {
    			  creategroup(socIn); //ajoute un nouvel elements dans la liste + ajouter les ip en parametre dans la liste qui est dans ce nouvel element
    		  }
    		  else if(line.equals("envoyer_a_groupe"))
    		  {
    			  
    			  choosegroup(socIn);
    			  //rentre un num de groupe
    			  //mettre ajour lattribut : groupchoisi
    			  //par defaut groupchoisu doit etre 0 , et ca correspond a la liste qui contient tout le monde
    		  }
    		  else
    		  {
    			//historique : on stocke le messge (expediteur + contenu)
        		  history.add(clientSocket.getInetAddress()+"-"+clientSocket.getPort()+" : "+line);
	    		  
	    		  
	    		  //for(Socket socket : clientsPorts.values()) //on envoie le message à tous les autres clients , si on veut parler à un groupe , on aura une liste avec toutes les sockets d'un meme groupe
	    			for(Socket socket : groups.get(groupeChoisi))
	    			  
	    		  {
	    			  PrintStream socOut = new PrintStream(socket.getOutputStream());
	    			  socOut.println(clientSocket.getInetAddress()+"-"+clientSocket.getPort()+" : "+line);
	    		  }
    		  }
    		  
    		}
    	} catch (Exception e) {
        	System.err.println("Error in clientThread:" + e); 
        }
       }

	private void choosegroup(BufferedReader socIn) throws IOException {
		// TODO Auto-generated method stub

		String groupnbr = socIn.readLine();
		groupeChoisi = Integer.parseInt(groupnbr);
	}

	private void creategroup(BufferedReader socIn) throws IOException {
		// TODO Auto-generated method stub
		 //pour GROUPE 
		
		String line = socIn.readLine();
		list_destinataire.add(clientSocket);
		while(!line.equals("1"))
		{
			list_destinataire.add(clientsPorts.get(line));
			line=socIn.readLine();
		}
		if(groups == null)
			System.out.println("groups est null dans clientthread");
		groups.add(list_destinataire);
		int groupID = groups.indexOf(list_destinataire); //a changer si on veut ajouter la fonctionalite supprimer groupe
		for(Socket socket : list_destinataire)
		{
				groupsNumber.get(socket.toString()).add(groupID);
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
