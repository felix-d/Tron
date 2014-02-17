package Client;
import java.awt.Color;

import javax.swing.JLabel;

public class TronPlayer {
	//boolean isAlive;
	int posX;
	int posY;
	int RGB;
	char direction;
	Color color;
	String username;
	String RGBstring;
	String machine;
	JLabel user;
	
	public TronPlayer(String username, String machineNameFromServer, String RGBstring, int posX, int posY){
		this.username = username;
		this.machine = machineNameFromServer;
		this.posX = posX;
		this.posY = posY;
		this.RGBstring = RGBstring;
		this.color = Color.decode(RGBstring);
	}

	public void checkIfDead() {
		if(direction=='X'){
			this.user.setForeground(Color.DARK_GRAY);
			this.color = Color.DARK_GRAY;
		}
		
	}
	
}
