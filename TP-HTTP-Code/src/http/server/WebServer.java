///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 3000");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        

        BufferedReader br = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        String request = br.readLine(); // Now you get GET index.html HTTP/1.1`
        String[] requestParam = request.split(" ");
      
        // remote is now the connected socket
  
        PrintWriter out = new PrintWriter(remote.getOutputStream());
        // Send the response
        // Send the headers
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Server: Bot");
        // this blank line signals the end of the headers
        out.println("");
        // Send the HTML page
        out.println("<H1>Welcome to the Ultra Mini-WebServer</H2>");
        switch(requestParam[0])
        {
        case "GET":
        	handleGET(out,requestParam);
        	break; 
        case "DELETE": 
        	handleDELETE(requestParam);
        	break;
        case "POST":
        	handlePOST(out,br);
        	break;
        }
        
        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
     

    
        out.flush();
        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }

  private void handlePOST(PrintWriter out, BufferedReader br) throws IOException {
	// TODO Auto-generated method stub
	  String str = ".";
      while (str != null )
      {
        str = br.readLine();
        System.out.println(str);
      }
  }

private void handleGET(PrintWriter out,String[] requestParam) throws IOException {
	// TODO Auto-generated method stub
	  File file = new File(requestParam[1].substring(1));
	  FileReader fr;
	try {
		fr = new FileReader(file);
		BufferedReader bfr = new BufferedReader(fr);
		  String line;
		  while ((line = bfr.readLine()) != null) {
		      out.println(line);
		  }

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	  
private void handleDELETE(String[] requestParam) {
	File file = new File (requestParam[1].substring(1));
	file.delete();
	
}
	



/**
   * Start the application.
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
