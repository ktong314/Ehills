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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import com.google.gson.Gson;

public class ClientMain extends Application {
	
	Socket clientSocket = null;
	PrintWriter toServer = null;
	BufferedReader fromServer = null;
	String curItemName = null;
	Gson gson = new Gson();
	Customer customer;
	GridPane itemMenu;
	GridPane information;
	ItemUI currentItemUI = null;
	Client client = null;
	ClientMain clientmain = this;
	ArrayList<String> purchaseHistory = new ArrayList<String>();

	public static void main(String[] args) {
        launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			clientSocket = new Socket("localhost", 4242);
			fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        toServer = new PrintWriter(clientSocket.getOutputStream());
	        
	        
	        
	        client = new Client(clientSocket, fromServer, toServer, this);
	        
	     
	        
	        
		} catch (IOException e) {
			System.out.println("Connection to server failed");
			System.exit(0);
		}
		
		GridPane loginMenu = new GridPane();
        loginMenu.setHgap(20);
        loginMenu.setVgap(20);
        
        
		
        Scene loginScene = new Scene(loginMenu, 700, 700);
        //loginScene.getStylesheets().add("stylesheet/styles.css");
        primaryStage.setTitle("EHills");
        primaryStage.setScene(loginScene);
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/hills.png")));
        primaryStage.show();
        
        Label username = new Label("Username: ");
        username.setFont(new Font("Times New Roman", 20));
        TextField usernameTextField = new TextField();
        usernameTextField.setEditable(true);
        usernameTextField.setPrefWidth(200);
        loginMenu.add(username, 1, 1);
        loginMenu.add(usernameTextField, 2, 1);
        
        Label password = new Label("Password: ");
        password.setFont(new Font("Times New Roman", 20));
        TextField passwordTextField = new TextField();
        passwordTextField.setEditable(true);
        passwordTextField.setPrefWidth(200);
        loginMenu.add(password, 1, 2);
        loginMenu.add(passwordTextField, 2, 2);
		
        Button login = new Button("Login");
        login.setMaxWidth(120);

        Button guest = new Button("Continue as Guest");
        guest.setMaxWidth(300);

        // Button to quit
        Button exit = new Button("Exit");
        exit.setMaxWidth(120);
        exit.setStyle("-fx-background-color: #8B0000;-fx-text-fill:#ecf0f1;");
        
        
        loginMenu.add(login, 1, 3);
        loginMenu.add(guest, 2, 3);
        loginMenu.add(exit, 3, 3);
        
        login.setOnAction(event -> {
        	sendToServer(new Message("LOGIN", usernameTextField.getText(), passwordTextField.getText()));
        	w();w();
        	
        	if(client.loginCheck) {
        		displayClientUI(primaryStage);
        	} else {
        		Label error = new Label("Incorrect Login");
        		error.setFont(new Font("Times New Roman", 20));
        		loginMenu.add(error, 3, 1);
        	}
        });
        
        guest.setOnAction(event -> {
        	client.loginCheck = true;
        	client.setName("guest");
        	displayClientUI(primaryStage);
        });
        
        exit.setOnAction(event -> {
        	System.exit(0);
        });
        
	}
	private void displayClientUI(Stage primaryStage) {
		MediaPlayer media = new MediaPlayer(new Media(new File("audio/creativeminds.mp3").toURI().toString()));
		media.play();
		TabPane itemsPane = new TabPane();
        Scene itemsMenu = new Scene(itemsPane, 700, 700);
        primaryStage.setScene(itemsMenu);
        primaryStage.show();
        Tab welcome = new Tab("Welcome " + client.getName());
        Tab shop = new Tab("Shop");
        Tab history = new Tab("History");
        shop.setClosable(false);
        welcome.setClosable(false);
        history.setClosable(false);
        
        Button exit = new Button("Exit");
        exit.setMaxWidth(100);
        exit.setStyle("-fx-background-color: #8B0000;-fx-text-fill:#ecf0f1;");
        exit.setOnAction(event -> {
        	System.exit(0);
        });
        
        
        itemsPane.getTabs().add(welcome);
        itemsPane.getTabs().add(shop);
        itemsPane.getTabs().add(history);
        history.setOnSelectionChanged(event -> {
        	BorderPane historyPane = new BorderPane();
            TextArea historyText = new TextArea();
            Message updateHistory = new Message("UPDATE_HISTORY");
            sendToServer(updateHistory);
            historyText.setEditable(false);
            historyText.setVisible(true);
            historyText.setPrefWidth(200);
            historyText.setText(getHistoryString());
            historyPane.setTop(historyText);
            historyPane.setLeft(exit);
            history.setContent(historyPane);
        });
        
        
        BorderPane welcomePane = new BorderPane();
        TextArea welcomeText = new TextArea();
        welcomeText.setEditable(false);
        welcomeText.setVisible(true);
        welcomeText.setPrefWidth(200);
        welcomeText.setText("Welcome back to E-Hills, " + client.getName()
        	+ "\nSelect the shop tab to bid for items"
        	+ "\nSelect the history tab to view transaction history"
        	+ "");
        welcomePane.setTop(welcomeText);
        welcomePane.setLeft(exit);
        welcome.setContent(welcomePane);
        
        
        itemsPane.getTabs().add(welcome);
        itemsPane.getTabs().add(shop);
        itemsPane.getTabs().add(history);
        
        sendToServer(new Message("UPDATE"));
    	
    	itemMenu = new GridPane();
    	itemMenu.setHgap(20);
        shop.setContent(itemMenu);
        
        VBox itemSelection = new VBox();
		itemSelection.setPrefSize(120, 500);
		Label ISlabel = new Label("Choose an item\nto buy");
		ISlabel.setFont(new Font("Times New Roman", 15));
		itemSelection.getChildren().add(ISlabel);
		
		itemMenu.add(itemSelection, 1, 0);
		
        information = new GridPane();
        information.setVgap(10);
        information.setHgap(10);
		information.setPrefSize(580, 200);
		itemMenu.add(information, 2, 0);
		information.setStyle("-fx-background-color: #d3d3d3");
    	
    	
        for (int i = 0; i < client.getItems().size(); i++) {
        	Item currentItem = client.getItems().get(i);
            System.out.println(currentItem);
            Button newButton = new Button(currentItem.getName());
            newButton.setOnAction(event -> {
            	currentItemUI = new ItemUI(currentItem, this.client, this, itemMenu, information);
            	currentItemUI.displayUI();
            	curItemName = currentItem.getName();    	
            });
            newButton.setPrefWidth(100);
            itemSelection.getChildren().add(newButton);
        }
        
        
        itemMenu.add(exit, 1, 1);
        
        //itemsScene.getStylesheets().add("stylesheet/styles.css");
	}
	
	private String getHistoryString() {
		String historyString = "";
		if(!purchaseHistory.isEmpty()) {
			for(String line : purchaseHistory) {
				historyString = historyString + "" + line + "\n";
			}
		}
		return historyString;
	}

	public synchronized void sendToServer(Message send) {
		toServer.println(gson.toJson(send));
    	toServer.flush();
    	w();
	}
	
	public void w() {
		try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	public synchronized void processRequest(Message message) {
		 try {
	            switch (message.getType()) {
	                case "LOGIN_SUCCESSFUL":
	                	client.loginCheck = true;
	                	client.setName(message.getCustomer().getFirst() + " " + message.getCustomer().getLast());
	                	break;
	                case "UPDATE_ITEMS":
	                	client.updateItems(message.getItems());
	                	break;
	                case "REFRESH":
	                	if(curItemName.equals(message.getItem().getName())) {
	                		currentItemUI.refreshUI();
	                	}
	                	purchaseHistory = message.getHistory();
	                	break;
	                case "HISTORY_UPDATED":
	                	purchaseHistory = message.getHistory();
	                	if(currentItemUI != null) {
	                		currentItemUI.refreshUI();
	                	}
	                	break;

	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}

	
	

}
