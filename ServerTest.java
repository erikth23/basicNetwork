package networking;

import javax.swing.JFrame;

public class ServerTest{
	public static void main(String args[]){
		Server im = new Server();
		im.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		im.startRunning();
	}
}
