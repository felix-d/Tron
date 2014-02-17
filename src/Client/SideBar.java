package Client;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class SideBar extends JPanel {
	
	public SideBar(){
		this.setBackground(Color.black);
	}
	public void addUserToSideBar(TronPlayer tronPlayer){
		
		String user = tronPlayer.username +"@"+tronPlayer.machine;
		tronPlayer.user = new JLabel(user);
		tronPlayer.user.setForeground(tronPlayer.color);
		this.add(tronPlayer.user);
	}
	public void clear() {
		// TODO Auto-generated method stub
		this.removeAll();
	}
	
}
