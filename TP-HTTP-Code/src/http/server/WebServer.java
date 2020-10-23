///A Simple Web Server (WebServer.java)

package http.server;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
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
      s = new ServerSocket(1234);
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
        String request;
      
        request = br.readLine(); // Now you get GET index.html HTTP/1.1`
        
        if(request == null)
        	System.out.println("la request est null : "+request);
        String[] requestParam = request.split(" ");
      
        // remote is now the connected socket
  
        OutputStream os = remote.getOutputStream();
        PrintWriter out = new PrintWriter(os);
       
        // Send the response
        // Send the headers
       // out.println("HTTP/1.0 200 OK");
       // out.println("Content-Type: text/html");
       // out.println("Content-Type: image/jpeg");
  
       // out.println("Server: Bot");
        // this blank line signals the end of the headers
        //out.println("");
        // Send the HTML page
       // out.println("<H1>Welcome to the Ultra Mini-WebServer</H2>");
       // out.flush();
       // remote.close();

        switch(requestParam[0])
        {
        case "GET":
        	handleGET(os,out,requestParam);
        	break; 
        case "DELETE": 
        	handleDELETE(requestParam);
        	break;
        case "POST":
        	handlePOST(out,br);
        	break;
        case "HEAD":
        	handleHEAD(out,requestParam);
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
	  System.out.println("cest un post");
	  String str = ".";
	  String contentType="";
	  
      while (str != null && !str.isEmpty())
      {
        str = br.readLine();
        if(str.contains("Content-Type:"))
        {
        	contentType=str.split(" ")[1];
        }
        System.out.println("echo : "+str);
      }
      //lecture du body
      String bodyLine=".";
      String [] bodyLineSepare = new String[100];
     /* while (str != null && !str.isEmpty())
      {
        str = br.readLine();
        System.out.println("echo : "+str);
      }*/
      System.out.println("le content type : "+contentType);
      if(contentType.equals("application/x-www-form-urlencoded")) //alors le donnes seront sur une seule ligne
      {
    	  bodyLine = br.readLine();
    	  bodyLineSepare= bodyLine.split("&");
      }
      for(int i=0;i<bodyLineSepare.length;i++)
      {
    	  String clee =bodyLineSepare[i].split("=")[0];
    	String valeur =bodyLineSepare[i].split("=")[1]; 
    	  System.out.println(clee+ "--->"+valeur);
      }
      
  }

private void handleGET(OutputStream os ,PrintWriter out,String[] requestParam) throws IOException {
	// TODO Auto-generated method stub
	String[] imageExt = {"jpeg", "jpg","png"};
	String[] videoExt = {"mpeg","mp4","avi"};
	String[] audioExt = {"mp3"};
	List<String> listImage = Arrays.asList(imageExt);
	List<String> listVideo = Arrays.asList(videoExt);
	List<String> listAudio =  Arrays.asList(audioExt);
	
	System.out.println("on recoit bien le get");
	String path = requestParam[1].substring(1);
	  File file = new File(path);
	  
	  if (!file.exists()){
			System.out.println("-------------ERROR 404-------------");
			out.println("HTTP/1.0 404 Not Found");
			out.println("");
			
	}
	  else {
	
	  System.out.println("le fichier a chercher : "+path);
	  FileReader fr;
	  String extension = "";

	  int i = path.lastIndexOf('.');
	  if (i > 0) {
	      extension = path.substring(i+1);
	  }
	  if(extension.equals("html"))
	  {
		  sendHTML(os,file,out);
	  }
	  else if(listImage.contains(extension))
	  {
		  System.out.println("on va envoyer une image");
		 
		  sendImage(os,file,out);
		 // sendVideo(os,file,out);
	  }
	  else if(listVideo.contains(extension))
	  {
		  sendVideo(os,file,out);
	  }
	  else if(listAudio.contains(extension))
	  {
		  sendAudio(os, file, out);
	  }
	  }
}
	  
private void sendImage(OutputStream os, File file, PrintWriter out) throws IOException {
	// TODO Auto-generated method stub
	
	
	String head1 ="HTTP/1.0 200 OK\n";
			String head2="Content-Type: image/jpeg\n";
			String head3="Server: Bot\n";
			String head4="\r\n";
    /*out.println("HTTP/1.0 200 OK");
	  	 out.println("Content-Type: image/jpeg");
	  	out.println("Server: Bot");
	    out.println("");*/
	    os.write(head1.getBytes());
	    os.write(head2.getBytes());
	    os.write(head3.getBytes());
	    os.write(head4.getBytes());
	BufferedImage bImage;
	try {
		bImage = ImageIO.read(file);
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ImageIO.write(bImage, "jpg", bos );
		    byte [] data = bos.toByteArray();
		    os.write(data);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
}

private void sendHTML(OutputStream os, File file, PrintWriter out) throws IOException {
	// TODO Auto-generated method stub

	

	out.println("HTTP/1.0 200 OK");

    out.println("Content-Type: text/html");
    out.println("Server: Bot");
    // this blank line signals the end of the headers
    out.println("");
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

private void sendVideo(OutputStream os,File myFile,PrintWriter out) throws IOException {
	// TODO Auto-generated method stub
	String head1 ="HTTP/1.0 200 OK\n";
	String head2="Content-Type: video/mp4\n";
	String head3="Server: Bot\n";
	String head4="\r\n";
os.write(head1.getBytes());
os.write(head2.getBytes());
os.write(head3.getBytes());
os.write(head4.getBytes());
     byte [] mybytearray  = new byte [(int)myFile.length()];
   
     FileInputStream fis;
	try {
		fis = new FileInputStream(myFile);
		 BufferedInputStream bis = new BufferedInputStream(fis);
	     bis.read(mybytearray,0,mybytearray.length);
			os.write(mybytearray,0,mybytearray.length);
		     os.flush();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
     
     System.out.println("Done.");
}

private void sendAudio(OutputStream os,File myFile,PrintWriter out) throws IOException {
	// TODO Auto-generated method stub
	String head1 ="HTTP/1.0 200 OK\n";
	String head2="Content-Type: audio/mp3\n";
	String head3="Server: Bot\n";
	String head4="\r\n";
os.write(head1.getBytes());
os.write(head2.getBytes());
os.write(head3.getBytes());
os.write(head4.getBytes());
     byte [] mybytearray  = new byte [(int)myFile.length()];
   
     FileInputStream fis;
	try {
		fis = new FileInputStream(myFile);
		 BufferedInputStream bis = new BufferedInputStream(fis);
	     bis.read(mybytearray,0,mybytearray.length);
			os.write(mybytearray,0,mybytearray.length);
		     os.flush();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
     
     System.out.println("Done.");
}


private void handleDELETE(String[] requestParam) {
	File file = new File (requestParam[1].substring(1));
	file.delete();
	
}
	
private void handleHEAD(PrintWriter out,String[] requestParam) throws IOException {
	 
	try {
		URL obj = new URL(requestParam[1]);
	    URLConnection conn = obj.openConnection();

		  
		    Map<String, List<String>> map = conn.getHeaderFields();
		    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
		        out.println("Key : " + entry.getKey() +
		                 " ,Value : " + entry.getValue());
		    }

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
