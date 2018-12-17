package networking;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server(){
		super("Instant Messenger");
		
		userText=new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
				}
				);
		add(userText, BorderLayout.SOUTH);
		
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(500, 300);
		setVisible(true);	
	}
	
	//set up and run the server
	public void startRunning(){
		try{
			server = new ServerSocket(6789, 100);
			while(true){
				try{	
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\nServer ended the connection!");
				}finally{
					close();
				}
				
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//wait for connection to server, then display connection info
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect...\n");
		connection = server.accept();
		showMessage("Now connected to "+connection.getInetAddress().getHostName());
	}
	
	//get streams to send and recieve data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("Streams are now set up!");
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = "You are now connected";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n"+message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\nIDK what was sent");
			}
			
		}while(!message.equals("CLIENT - END"));
	}
	
	//close streams and sockets after you are done chatting
	private void close(){
		showMessage("\nClosing connections...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//send message to client
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER - "+message);
			output.flush();
			showMessage("\nSERVER - "+message);
		}catch(IOException ioException){
			chatWindow.append("\nERROR: CAN'T SEND MESSAGE");
		}
	}
	
	//updates chat window
	private void showMessage(final String text){
		SwingUtilities.invokeLater(//allows constant thread of messages instead of creating new GUI
				new Runnable(){
					public void run(){
						chatWindow.append(text);
					}
				}
				);
	}
	
	//let the user type into their box
	private void ableToType(final boolean type){
		SwingUtilities.invokeLater(//allows constant thread of messages instead of creating new GUI
				new Runnable(){
					public void run(){
						userText.setEditable(type);
					}
				}
				);
	}

}
