package Client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class TronGrid extends JComponent {
	
	int step  = 6;
	int gridWidth;
	int gridHeight;
	int[][] array;
	Vector<TronPlayer>players; 
	int thickness = 1;
	TronClient client;
	boolean dessinerEnceinte = false;
	public TronGrid(Vector<TronPlayer>players,TronClient client)
	{
	this.players = players;
	this.client= client;
	}

	void setSizeOfGrid(int gridWidth,int gridHeight)
	{
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		array = new int[gridWidth][gridHeight];
	}

	public void paintComponent(Graphics g) 
	{	
		if(dessinerEnceinte)
		{
			for(int i=0;i<array.length;i++)
			{
				for(int j=0;j<array[i].length;j++)
				{
					if(array[i][j]==3001)
					{
						g.setColor(Color.white);
						g.fillRect(i,j , 2, 2);
					}
					if(client.debutPartie)
					{
						for(int k=0;k<players.size();k++)
						{
							if(array[i][j]==k+1)
							{
								g.setColor(players.elementAt(k).color);
								g.fillRect(i,j , thickness, thickness);
							}

						}
					}
				}
			}
		}
	}

	synchronized public void faireAvancerJoueurs() {
		for(int i=0;i<players.size();i++)
		{
			TronPlayer pTemp = players.elementAt(i);
			if(pTemp.direction=='N'){
				
				pTemp.posY-=1;
			}
			else if(pTemp.direction=='S'){
				
				pTemp.posY+=1;
			}
			else if(pTemp.direction=='W'){
				
				pTemp.posX-=1;
			}
			else if(pTemp.direction=='E'){
				
				pTemp.posX+=1;
			}
			array[pTemp.posX][pTemp.posY] = i+1;
		}
		repaint();
		// TODO Auto-generated method stub
	}
	
	public void dessinerMurEnceinte(){
		for(int i=0;i<array.length;i++){
			array[i][0] = 3001;
			array[i][array[0].length-1] = 3001;
		}
		for(int j=0;j<array[0].length;j++){
			array[0][j] = 3001;
			array[array.length-1][j] = 3001;
		}
		dessinerEnceinte = true;
		repaint();
	}
	
	
}


