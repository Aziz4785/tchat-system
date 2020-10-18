
package stream;
/*
import java.io.*;
import java.net.*;

public class EchoServer  {


	static void doService(Socket clientSocket) {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		while (true) {
    		  String line = socIn.readLine();
    		  socOut.println(line);
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  

       public static void main(String args[]){ 
        ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
         // System.exit(1);
  	}
	try {
		listenSocket = new ServerSocket(1240); //port
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("connexion from:" + clientSocket.getInetAddress());
			doService(clientSocket);
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }
*/
