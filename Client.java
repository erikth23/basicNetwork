package networking;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message;
	private String serverIP;
	private Socket connection;
	
	//constructor
	public Client(String host){
		super("Client");
		serverIP=host;
		userText = new JTextField();
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
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(500, 300);
		setVisible(true);
	}
	
	//begin running
	public void StartRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException){
			showMessage("\nClient terminated the connection");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			close();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException{
		showMessage("Attempting to connect...\n");
		connection=new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to "+connection.getInetAddress().getHostName());
	}
	
	//set up streams to send and receive messages
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("You are now connected!\n");
	}
	
	//while chatting with server
	private void whileChatting() throws IOException{
		ableToType(true);
		do{
			try{
				message=(String)input.readObject();
				showMessage("\n"+message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("IDK that object type");
			}
		}while(!message.equals("Server - END"));
	}
	
	//closes the streams and sockets
	private void close(){
		showMessage("\nClosing Instant Messenger");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//send messages to server
	private void sendMessage(String m){
		try{
			output.writeObject("CLIENT - "+m);
			output.flush();
			showMessage("CLIENT - "+m);
		}catch(IOException ioException){
			chatWindow.append("\nSomething messed up sending the message");
		}
	}
	
	//shows message in chat window
	private void showMessage(final String m){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append("\n"+m);
					}
				}
				);
	}
	
	//gives user ability to type
	private void ableToType(final boolean type){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(type);
					}
				}
				);
	}
}
