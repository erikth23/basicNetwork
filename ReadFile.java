package networking;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ReadFile extends JFrame{
	
	private JTextField addressBar;
	private JEditorPane display;
	
	public ReadFile(){
		super("Erik's Browser");
		
		addressBar = new JTextField("enter a URL");
		addressBar.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadInfo(e.getActionCommand());
					}
				}
				);
		
		add(addressBar, BorderLayout.NORTH);
		
		display = new JEditorPane();
		display.setEditable(false);
		display.addHyperlinkListener(
				new HyperlinkListener(){
					public void hyperlinkUpdate(HyperlinkEvent e){
						if(e.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
							loadInfo(e.getURL().toString());
						}
					}
				}
				);
		
		add(new JScrollPane(display), BorderLayout.CENTER);
		setSize(500, 300);
		setVisible(true);
	}
	
	private void loadInfo(String userURL){
		try{
			display.setPage(userURL);
			addressBar.setText(userURL);
		}catch(Exception e){
			System.out.println("Error!");
		}
	}

}
