package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class TronClient extends JFrame implements KeyListener
{
	String hostname;
	int serverport;
	int gridWidth = 0;
	int gridHeight = 0;
	public TronClientConnection tcc;
	Vector <TronPlayer> players;
	TronGrid grid;
	SideBar sideBar;
	Container cont;
	JLabel wait;
	public boolean debutPartie = false;
	JLabel attendez;
	TronClient(String firstArg, String secondArg)
	{
		hostname = firstArg;
		serverport = Integer.parseInt(secondArg);
		
		players = new Vector<TronPlayer>();
		grid = new TronGrid(players,this);
		cont = getContentPane();
		this.addComponentsToPane();
		
		TronClientConnection clientConnection= new TronClientConnection(this,grid);
		clientConnection.start();
		this.gridWidth = grid.gridWidth;
		this.gridHeight = grid.gridHeight;
		sideBar = new SideBar();
		this.addKeyListener(this);
		this.setTitle("TRON");
		
		
		((JComponent) cont).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.getContentPane().setBackground(Color.black);
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		if(args.length!=2)
		{
			System.out.println("L'utilisation correcte du serveur est  java TronClient <serverhostname> <serverport> ");
			System.exit(1);
		}
		else{
			TronClient client = new TronClient(args[0],args[1]);
			
			
		}
		
		

	}

	void sendUsername(TronClientConnection tc, String username)
	{
		tc.output.println(tc.username);
		tc.output.flush();
	}

	void setPlayer(TronClientConnection tcc)
	{
		this.tcc = tcc;
	}

	public void keyPressed(KeyEvent e) 
	{
		// snake move to left when player pressed left arrow
		if (e.getKeyCode() == 37) {//gauche
			if(tcc.direction!='E')tcc.direction = 'W';
		}
		else if (e.getKeyCode() == 40) {//bas
			tcc.direction = 'S';
		}
		else if (e.getKeyCode() == 38) {//haut
			tcc.direction = 'N';
		}
		else if (e.getKeyCode() ==  39) {//droot
			tcc.direction = 'E';
		}
		tcc.output.println(tcc.direction);
		tcc.output.flush();
	}
	
	public void keyReleased(KeyEvent e) 
	{
	}

	public void keyTyped(KeyEvent e) 
	{
	}
	public void addComponentsToPane() 
	{  
		Container pane = this.getContentPane();
		attendez = new JLabel("VEUILLEZ ATTENDRE...");
		attendez.setForeground(Color.white);
		attendez.setPreferredSize(new Dimension(300,30));
		pane.add(attendez, BorderLayout.BEFORE_FIRST_LINE);
	}
		
	
	public void setGridSize(int width, int height)
	{
		Container pane = this.getContentPane();
		this.getContentPane().removeAll();
		pane.add(grid, BorderLayout.LINE_START);
        pane.add(sideBar, BorderLayout.LINE_END);
		grid.setSizeOfGrid(width, height);
		grid.setPreferredSize(new Dimension(width,height));
		sideBar.setPreferredSize(new Dimension(200,200));
		this.pack();
	}
	
	
	public void clearGame()
	{
		grid.array = new int[grid.gridWidth][grid.gridHeight];
		grid.dessinerMurEnceinte();
		grid.repaint();
		sideBar.removeAll();
		sideBar.repaint();
	}

	


}