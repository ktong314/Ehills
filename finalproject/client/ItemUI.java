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



import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class ItemUI {
	private Item item;
	private Client client;
	private ClientMain clientMain;
	private GridPane information;
	private GridPane commands;
	//private GridPane description;
	
	public ItemUI(Item item, Client client, ClientMain clientMain, GridPane itemMenu, GridPane information){
		this.item = item;
		this.client = client;
		this.clientMain = clientMain;
		this.information = information;
		commands = new GridPane();
        commands.setVgap(10);
        commands.setHgap(10);
		commands.setPrefSize(580, 800);
		itemMenu.add(commands, 2, 1);
		commands.setStyle("-fx-background-color: #d3d3d3");
	}
	
	public void displayUI() {
		Message message = new Message("UPDATE");
		clientMain.sendToServer(message);	
		information.getChildren().clear();
		commands.getChildren().clear();
		
		
		
		
		Text itemName = new Text("Item: " + item.getName());
		itemName.setFont(new Font("Times New Roman", 25));
		Text description = new Text("Description:\n" + item.getDescription());
		description.setFont(new Font("Times New Roman", 15));
		Text buyOut = new Text("Buy Now: " + item.getBuyOut());
		buyOut.setFont(new Font("Times New Roman", 15));
		Text minBid = new Text("Current Price: " + Math.max(item.getCurrentPrice(), item.getminBid()));
		minBid.setFont(new Font("Times New Roman", 15));
		Text highestBid = new Text("Highest Bidder: " + item.getHighestBidder());
		highestBid.setFont(new Font("Times New Roman", 15));
		
		Text buyNowInstructions = new Text("Buy now for: ");
		buyNowInstructions.setFont(new Font("Times New Roman", 15));
		TextField buyNowPrice = new TextField("" + item.getBuyOut());
		Button buyNow = new Button("Buy " + item.getName() + " Now");
		buyNow.setOnAction(event -> {
			item.setCurrentPrice(item.getBuyOut());
			item.setSold(true);
			item.setHighestBiddet(client.getName());
			Message outgoing = new Message("ITEM_BOUGHT", item, client.getName());
			clientMain.sendToServer(outgoing);
			displayUI();
		});
		Text bidInstructions = new Text("Enter your bid: ");
		bidInstructions.setFont(new Font("Times New Roman", 15));
		TextField bidPrice = new TextField("" + Math.max(item.getCurrentPrice(), item.getminBid()));
		Button bid = new Button("Bid on " + item.getName());
		bid.setOnAction(event -> {
			try {
				double price = Double.parseDouble(bidPrice.getText());
				if(price >= item.getminBid() && price > item.getCurrentPrice() && item.getCurrentPrice() <= item.getBuyOut() && item.getSold() == false) {
					item.setCurrentPrice(price);
					item.setHighestBiddet(client.getName());
					if(item.getCurrentPrice() == item.getBuyOut()) {
						item.setSold(true);
					}
					Message outgoing = new Message("ITEM_BOUGHT", item, client.getName());
					clientMain.sendToServer(outgoing);
					displayUI();
				}
				else {
					bidInstructions.setText("invalid bid");
				}
			} catch (NumberFormatException e) {
				bidInstructions.setText("input a number!");
			}
		});
		
		Button refresh = new Button("Refresh");
		refresh.setOnAction(event -> {
			displayUI();
		});
		
		
		
		information.add(itemName, 1, 0);
		information.add(description, 1, 1);
		information.add(buyOut, 1, 2);
		information.add(minBid, 1, 3);
		information.add(highestBid, 1, 4);
		commands.add(buyNowInstructions, 1, 1);
		commands.add(buyNowPrice, 2, 1);
		commands.add(buyNow, 3, 1);
		commands.add(bidInstructions, 1, 2);
		commands.add(bidPrice, 2, 2);
		commands.add(bid, 3, 2);
		commands.add(refresh, 1, 3);
		
		if(item.getSold()) {
			buyNow.setDisable(true);
			bid.setDisable(true);
			Text soldOut = new Text("ITEM SOLD OUT");
			soldOut.setFont(new Font("Times New Roman", 35));
			soldOut.setStyle("-fx-text-fill: #ff0000");
			commands.add(soldOut, 2, 3);
		}
		
		
	}
	public void refreshUI() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				displayUI();
				
			}
			
		});
		
	}
}
