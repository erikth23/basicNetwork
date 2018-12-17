package networking;

import javax.swing.JFrame;
public class ClientServer{
	public static void main(String args[]){
		Client myClient;
		myClient = new Client("127.0.0.1");
		myClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myClient.StartRunning();
	}
}
