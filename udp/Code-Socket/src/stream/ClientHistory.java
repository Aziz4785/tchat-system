package stream;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
public class ClientHistory extends Thread {
	
private MulticastSocket clientmulticastSocket;
	
	//des quil recoit une list du serveur , il laffiche

	DatagramSocket clientSock;
	int serverPort;
	InetAddress hostname;
	
	ClientHistory(DatagramSocket clientSock,InetAddress hostname, int port) throws UnknownHostException {
		this.clientSock= clientSock;
		this.hostname=hostname;
		this.serverPort=port;
	}

	public void run() {

		try {
			while(true) {
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				clientSock.receive(packet);
				ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
			    LinkedList<String> history = (LinkedList<String>) inputStream.readObject();
			    for(String str : history)
			    {
			    	System.out.println(str);
			    }
	     }
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
