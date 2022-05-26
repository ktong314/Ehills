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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.Reader;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

import com.google.gson.Gson;

public class Server extends Observable{
	
	private ArrayList<Customer> customers;
	private ArrayList<Item> items;

	private ArrayList<String> history = new ArrayList<String>();

	public static void main(String[] args) {
		Server server = new Server();
		server.populateItems();
		server.populateLogins();
		try {
			server.SetupNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("server set up.");
	}

	private void SetupNetworking() throws Exception{
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSock = new ServerSocket(4242);
			while(true) {
				Socket clientSocket = serverSock.accept();
				System.out.println("Connecting to... " + clientSocket);
				ClientHandler handler = new ClientHandler(this, clientSocket);
				addObserver(handler);
				Thread t = new Thread(handler);
				t.start();
				System.out.println("Able to connect");
			}
		} catch (IOException e) {
			System.out.println("Unable to connect");
		}
		
		
	}

	private void populateLogins() {
		customers = new ArrayList<Customer>();
		Gson gson = new Gson();
		File customerFile = new File("customers");
		String[] files = customerFile.list();
		for(int i = 0; i < files.length; i++) {
			String file = "customers/" + files[i];
			try {
				Reader reader = new FileReader(file);
				Customer customer = gson.fromJson(reader, Customer.class);
				customers.add(customer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(customers);
	}

	private void populateItems() {
		items = new ArrayList<Item>();
		Gson gson = new Gson();
		File customerFile = new File("items");
		String[] files = customerFile.list();
		for(int i = 0; i < files.length; i++) {
			String file = "items/" + files[i];
			try {
				Reader reader = new FileReader(file);
				Item item = gson.fromJson(reader, Item.class);
				items.add(item);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(items);
		
	}
	
	protected synchronized void processRequest(String input, Socket clientSocket) throws IOException {
		PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
		Gson gson = new Gson();
        Message fromClient = gson.fromJson(input, Message.class);
        try {
        	switch(fromClient.getType()) {
        		case "LOGIN": 
        			for (Customer currentCustomer : customers) {
                        if (currentCustomer.getUser().equals(fromClient.getUsername()) && currentCustomer.getPassword().equals(fromClient.getPassword())) {
                            Message message = new Message("LOGIN_SUCCESSFUL", currentCustomer);
                            toClient.println(gson.toJson(message));
                            toClient.flush();
                            this.setChanged();
                            break;
                        }
                    }
        			break;
        		case "UPDATE":
        			Message message = new Message("UPDATE_ITEMS", items);
        			toClient.println(gson.toJson(message));
        			toClient.flush();
        			this.setChanged();
        			break;
        		case "ITEM_BOUGHT":
        			for(Item currentItem : items) {
        				if(currentItem.getName().equals(fromClient.getItem().getName())){
        					currentItem.setCurrentPrice(fromClient.getItem().getCurrentPrice());
        					currentItem.setSold(fromClient.getItem().getSold());
        					currentItem.setHighestBiddet(fromClient.getItem().getHighestBidder());
        				}
        			}
        			this.setChanged();
        			System.out.println(countObservers());
        			String log = fromClient.getFullname() + " bid $" + fromClient.getItem().getCurrentPrice() + " on " + fromClient.getItem().getName();
                	if(fromClient.getItem().getSold()){
                		log = fromClient.getFullname() + " bought " + fromClient.getItem().getName() + " for $" + fromClient.getItem().getCurrentPrice();
                	}
                	
                	history.add(log);
                	
        			this.notifyObservers(gson.toJson(new Message("REFRESH", fromClient.getItem(), history)));
        			
        			break;
        		case "UPDATE_HISTORY":
        			toClient.println(gson.toJson(new Message("HISTORY_UPDATED", fromClient.getItem(), history)));
        			toClient.flush();
        			break;
        			
        		
        	}
        } catch (Exception e) {
        	System.out.println("error");
        }
	}

}
