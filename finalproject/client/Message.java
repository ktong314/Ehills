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

import java.io.Serializable;
import java.util.ArrayList;




public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String type;
	private String password;
	private String username;
	private String command;
	private Customer user;
	private Item item;
	private String fullname;
	private ArrayList<Item> items;
	private ArrayList<String> history;

	public Message(String type, String username, String password) {
		this.type = type;
		this.username = username;
		this.password = password;
	}
	
	public Message(String type, String fullname) {
		this.type = type;
		this.fullname = fullname;
	}
	
	public Message(String type, Customer c) {
		this.type = type;
		this.user = c;
	}
	
	public Message(String type, Item i, String fullname) {
		this.type = type;
		this.item = i;
		this.fullname = fullname;
	}
	
	public Message(String type, Item i, ArrayList<String> history) {
		this.type = type;
		this.item = i;
		this.history = history;
	}
	
	public Message(String type, ArrayList<Item> items) {
		this.items = items;
		this.type = type;
	}
	
	public Message(String type) {
		this.type = type;
	}
	
	public Message(String type, ArrayList<Item> items, Item item) {
		this.items = items;
		this.type = type;
		this.item = item;
	}
	
	public ArrayList<String> getHistory(){
		return history;
	}
	public Item getItem() {
		return item;
	}
	public String getFullname() {
		return fullname;
	}
	public ArrayList<Item> getItems() {
		return items;
	}
	public Customer getCustomer() {
		return user;
	}
	public String getCommand() {
		return command;
	}
	public String getType() {
		return type;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}