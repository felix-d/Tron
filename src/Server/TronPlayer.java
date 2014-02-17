package Server;
import java.awt.Color;

import javax.swing.JLabel;

public class TronPlayer {
	//boolean isAlive;
	int posX;
	int posY;
	int[]pos;
	int RGB;
	int id;
	char direction;
	Color color;
	String username;

	public TronPlayer(int[]pos, int RGB, char direction, int id){
		//this.isAlive = true;
		this.pos = pos;
		this.posX = pos[0];
		this.posY = pos[1];
		this.direction = direction;
		this.RGB = RGB;
		this.id = id;
	}
	//Constructeur de TronPlayer qui rejoue
	public TronPlayer(TronPlayer TP, int[]pos, char direction){
		this.pos = pos;
		this.posX = pos[0];
		this.posY = pos[1];
		this.direction = direction;
		this.RGB = TP.RGB;
		this.id = TP.id;
		this.username = TP.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	public void kill() {
		this.direction = 'X';
	}
	
	
}
