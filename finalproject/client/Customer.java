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

public class Customer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	
	public Customer(String username, String Password, String firstName, String lastName) {
		this.username = username;
		this.password = Password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	
	public String getUser() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirst() {
		return firstName;
	}
	
	public String getLast() {
		return lastName;
	}
	
	@Override 
	public String toString() {
		return "{" + firstName + " " + lastName + " | username: " + username + " | password: " + password + "}";
	}
}

