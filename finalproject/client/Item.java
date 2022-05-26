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

public class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double minBid;
	private double buyOut;
	private String description;
	private String highestBidder;
	private boolean sold;
	private double currentPrice;
	
	
	
	public Item(String name, double minBid, double buyOut, String description, String highestBidder, double currentPrice) {
		this.name = name;
		this.minBid = minBid;
		this.buyOut = buyOut;
		this.description = description;
		this.highestBidder = highestBidder;
		this.sold = false;
		this.currentPrice = currentPrice;
	}
	
	@Override
	public String toString() {
		return "{" + name + " | minimum bid: " + minBid + " | buy out: " + buyOut + " | description: " + description + "}";
	}
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
 	
	public String getDescription() {
		return description;
	}
	public String getHighestBidder() {
		return highestBidder;
	}
	public void setHighestBiddet(String highestBidder) {
		this.highestBidder = highestBidder;
	}
	public String getName() {
		return name;
	}
	

	
	public double getminBid() {
		return minBid;
	}
	
	public double getBuyOut() {
		return buyOut;
	}
	
	public boolean getSold() {
		return sold;
	}
	
	public void setSold(boolean sold) {
		this.sold = sold;
	}
	
}
