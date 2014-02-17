package Server;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.awt.Color;
import java.io.*;

public class TronServer 
{

	int gridWidth;
	int gridHeight;
	boolean debutPartie;
	boolean peutEtreDeconnecte;
	boolean joueurPeutConnecter;
	int nbDeClients;
	int port;
	int clockTick;
	int [][] array;
	String[]args;
	ServerSocket serverSocket;
	Vector<PlayerConnection> tabDeClients;
	Vector<Integer>allPosX;
	Vector<Integer>allPosY;
	Vector<Color>allColors;
	
	TronServer(String[] args)
	{
		this.args = args;
		this.port = Integer.parseInt(args[0]);
		this.clockTick = Integer.parseInt(args[1]);
		this.gridWidth = Integer.parseInt(args[2]);
		this.gridHeight = Integer.parseInt(args[3]);
		this.debutPartie = false;
		this.peutEtreDeconnecte = false;
		this.array = new int[gridWidth][gridHeight];
		this.DessinerMurEnceinte();
		this.joueurPeutConnecter = true;
		this.tabDeClients = new Vector<PlayerConnection>();
		this.allPosX = new Vector<Integer>();
		this.allPosY = new Vector<Integer>();
		this.allColors = new Vector<Color>();
		
		try 
		{
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		if(args.length!=4)
		{
			System.out.println("L'utilisation correcte du serveur est  java TronServer <serverport> <clocktick> <gridwidth> <gridheight> ");
			System.exit(1);
		}
		
		TronServer tronServer = new TronServer(args);
		
		try
		{
			printWelcome(tronServer.port);
			new TronHeartBeat(tronServer);
			
			//boucle infinie
			while (true)
			{
				Socket clientSocket = tronServer.serverSocket.accept();
				if(tronServer.joueurPeutConnecter) 
				{
					PlayerConnection player = new PlayerConnection(clientSocket, tronServer);
					player.start();
				}
				else
				{
					PlayerStandBy player = new PlayerStandBy(clientSocket, tronServer);
					player.start();
				}
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Message de demarrage du serveur.
	static void printWelcome(int port)
	{
		System.out.println("TronServer");
		System.out.println("--------");
		System.out.println("Demarre sur le port : "+port);
	}

	//Fonction qui envoie des messages a tous les joueurs du vecteur 'players'
	synchronized public void sendAll(String message)
	{
		PrintWriter out;
		PlayerConnection pc;
		for (int i = 0; i < tabDeClients.size(); i++)
		{
			pc =  (PlayerConnection) tabDeClients.elementAt(i);
			out = pc.output; 
			if (out != null) 
			{
				out.println(message);
				out.flush();
			}
		}
	}
	
	//Envoie de la taille du tableau aux clients
	synchronized public void sendSize(PlayerConnection pc)
	{
		pc.output.println(gridWidth);
		pc.output.println(gridHeight);
		pc.output.flush();
	}
	
	// Methode : ajoute un nouveau client dans la liste
	synchronized public int addClient(PlayerConnection pc)
	{
		nbDeClients++; 
		tabDeClients.addElement(pc);
		return tabDeClients.size()-1;
	}
	
	//Methode qui supprime un client du vecteur players
	 synchronized public void delClient(int i)
	  {
	    nbDeClients--;
	    if (tabDeClients.elementAt(i) != null) // l'element existe ...
	    {
	      tabDeClients.removeElementAt(i); // ... on le supprime
	    }
	  }

	// Methode : retourne le nombre de clients connectes
	synchronized public int getNbClients()
	{
		return nbDeClients;
	}

	//Methode qui envoie l'info de tous les utilisateurs au target
	synchronized public void sendAllUsersToTarget(PlayerConnection pcTarget)
	{
		for(int i=0;i<tabDeClients.size()-1;i++){
			PlayerConnection pc=(PlayerConnection)tabDeClients.elementAt(i);
			String message = "+"+pc.tronPlayer.username +"\n"+
					pc.clientSocket.getInetAddress().getHostName()+"\n"+
					pc.tronPlayer.RGB+"\n"+
					pc.tronPlayer.posX+"\n"+
					pc.tronPlayer.posY;
			pcTarget.output.println(message);
			pcTarget.output.flush();
			
		}
	}
	
	//Methode qui envoie l'info d'un joueur a tous les utilisateurs 
	synchronized public void sendUserToAll(PlayerConnection pc)
	{
		PrintWriter out; 
		out = pc.output;
		if (out != null) 
		{
			String message ="+"+pc.tronPlayer.username +"\n"+
					pc.clientSocket.getInetAddress().getHostName()+"\n"+
					pc.tronPlayer.RGB+"\n"+
					pc.tronPlayer.posX+"\n"+
					pc.tronPlayer.posY;
			sendAll(message);
		}
	}
	
	//Choix d'une position initiale au hasard.
	synchronized public int[] setInitialPosition()
	{
		int[]position = new int[2];
		boolean AJOUTE = false;
		int posX = 0;
		int posY = 0;
		while(!AJOUTE){
			posX = 1+(int) (Math.random()*(this.gridWidth-2));
			posY = 1+(int)(Math.random()*(this.gridHeight-2));
			if(allPosX.isEmpty()&&allPosY.isEmpty())
			{
				AJOUTE = true;
			}
			else if(allPosX.contains(posX)&&allPosY.contains(posY))
			{
				AJOUTE = false;
			}
			else
			{
				AJOUTE = true;
			}
		}
		allPosX.add(posX);
		allPosY.add(posY);
		position[0]= posX;
		position[1]= posY;
		return position;
	}
	
	//Choix d'une couleur au hasard
	synchronized public int setColor()
	{
		boolean AJOUTE = false;
		Color color = null;
		int RGBrepresentation;
		while(!AJOUTE)
		{
			Random random = new Random();
			float hue = random.nextFloat();
			// Saturation between 0.1 and 0.3
			float saturation = (random.nextInt(2000) + 7000) / 10000f;
			float luminance = 2f;
			color = Color.getHSBColor(hue, saturation, luminance);
			if(allColors.isEmpty()) AJOUTE = true;
			else if (allColors.contains(color)) AJOUTE = false;
			else AJOUTE = true;
		}
		RGBrepresentation = color.getRGB();
		return RGBrepresentation;
	}
	
	//Methode qui place le client dans le array
	synchronized public void DessinerJoueur()
	{
		for(int i=0;i<tabDeClients.size();i++)
		{	
			PlayerConnection pc = (PlayerConnection) tabDeClients.elementAt(i);
			array[pc.tronPlayer.posX][pc.tronPlayer.posY] = pc.tronPlayer.id;
		}
	}
	
	//Methode qui dessine le mur d'enceinte pour detecter les collisions
	synchronized public void DessinerMurEnceinte(){
		for(int i=0;i<array.length;i++){
			array[i][0] = 3001;
			array[i][array[0].length-1] = 3001;
		}
		for(int j=0;j<array[0].length;j++){
			array[0][j] = 3001;
			array[array.length-1][j] = 3001;
		}
	}

	//
	synchronized public void FaireAvancerLesJoueurs()
	{	
		for(int i=0;i<tabDeClients.size();i++)
		{	
			PlayerConnection pc = (PlayerConnection) tabDeClients.elementAt(i);
			if(pc.tronPlayer.direction=='N')
			{
				if(array[pc.tronPlayer.posX][pc.tronPlayer.posY-1]!=0)pc.tronPlayer.kill();
				else pc.tronPlayer.posY-=1;
			}
			else if(pc.tronPlayer.direction=='S')
			{
				if(array[pc.tronPlayer.posX][pc.tronPlayer.posY+1]!=0)pc.tronPlayer.kill();
				else pc.tronPlayer.posY+=1;
			}
			else if(pc.tronPlayer.direction=='E')
			{
				if(array[pc.tronPlayer.posX+1][pc.tronPlayer.posY]!=0)pc.tronPlayer.kill();
				else pc.tronPlayer.posX+=1;
			}
			else if(pc.tronPlayer.direction=='W')
			{
				if(array[pc.tronPlayer.posX-1][pc.tronPlayer.posY]!=0)pc.tronPlayer.kill();
				else pc.tronPlayer.posX-=1;
			}
		}
		DessinerJoueur();
	}
	synchronized public void sendAllPlayersPositions(){
		for(int i=0;i<tabDeClients.size();i++){
			PlayerConnection pc=(PlayerConnection)tabDeClients.elementAt(i);
			String message = "+"+pc.tronPlayer.username +"\n"+
					pc.clientSocket.getInetAddress().getHostName()+"\n"+
					pc.tronPlayer.RGB+"\n"+
					pc.tronPlayer.posX+"\n"+
					pc.tronPlayer.posY;
			sendAll(message);
		}
		
	}
	synchronized public char setInitialDirection() {
		char direction = 0;
		int pick = (int)(Math.random()*4);
		switch(pick){
		case 0:
			direction='N';
			break;
		case 1:
			direction='S';
			break;
		case 2:
			direction ='E';
			break;
		case 3:
			direction = 'W';
			break;
		}
		return direction;
	}
}
