/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Kevin Tong>
* <kyt259>
* <17360>
* Slip days used: <1>
* Spring 2022
*/
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

public class Client {

	private String name;
	private ArrayList<Item> items;
	public boolean loginCheck = false;
	Scanner in = new Scanner(System.in);
	Gson gson = new Gson();
	
	
	public Client(Socket socket, BufferedReader reader, PrintWriter writer, ClientMain clientMain) {

		
		Thread readerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				String output;
	            try {
	                while(true){
	                    if((output = reader.readLine()) != null) {
	                    Message message = gson.fromJson(output, Message.class);
	                    System.out.println(name + "receiving message from server: " + output);
	                    clientMain.processRequest(message);
	                    }
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
				
			}	
        });
		
		
		
		readerThread.start();
		
		
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public void updateItems(ArrayList<Item> serverItems) {
		if(items == null) {
			items = serverItems;
		}
		else {
			for(int i = 0; i < items.size(); i++) {
				items.get(i).setCurrentPrice(serverItems.get(i).getCurrentPrice());
				items.get(i).setSold(serverItems.get(i).getSold());
				items.get(i).setHighestBiddet(serverItems.get(i).getHighestBidder());
			}
		}
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
