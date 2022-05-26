/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Kevin Tong>
* <kyt259>
* <17360>
* Slip days used: <1>
* Spring 2022
*/
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import com.google.gson.Gson;

public class ClientHandler extends PrintWriter implements Runnable, Observer {
	private Server server;
	Socket clientSocket;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	Gson gson = new Gson();
	
    protected ClientHandler(Server server, Socket clientSocket) throws IOException {
        super(clientSocket.getOutputStream());
        this.server = server;
        this.clientSocket = clientSocket;
        fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        toClient = new PrintWriter(this.clientSocket.getOutputStream());

    }

	@Override
	public void update(Observable o, Object arg) {
		sendToClient((String) arg);
		
	}

	private void sendToClient(String arg) {
		System.out.println("Sending to client: " + arg);
	    toClient.println(arg);
	    toClient.flush();
	}
	
	@Override
	public void run() {
		String input;
		try {
		      while (true) {
		    	  if((input = fromClient.readLine()) != null) {
				        System.out.println("From client: " + input);
				        server.processRequest(input, clientSocket);
		      	}
		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}

}
