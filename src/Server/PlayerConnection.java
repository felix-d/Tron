package Server;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.net.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


class PlayerConnection extends Thread {

	Socket clientSocket;
	PrintWriter output;
	BufferedReader input;
	private TronServer tronServer;
	TronPlayer tronPlayer;

	public PlayerConnection(Socket s, TronServer tronServer) 
	{
		this.clientSocket = s;
		this.tronServer = tronServer;
		try 
		{
			output = new PrintWriter(s.getOutputStream());
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			tronPlayer = new TronPlayer(tronServer.setInitialPosition(),tronServer.setColor(),tronServer.setInitialDirection(),tronServer.addClient(this)+1);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//THREAD RUN();
	public void run() 
	{
		try 
		{
			String ligneDuClient="";
			System.out.println("Un nouveau client s'est connecte, no "+tronPlayer.id);
			tronServer.sendSize(this);
			tronPlayer.setUsername(input.readLine());
			System.out.println("New User: "+tronPlayer.username);
			tronServer.sendAllUsersToTarget(this);
			tronServer.sendUserToAll(this);
			while((ligneDuClient=input.readLine())!=null)
			{
				if(tronPlayer.direction !='X'){
					char directionChoisie =ligneDuClient.charAt(0);
					if(tronServer.debutPartie==false||(tronPlayer.direction=='W'&&directionChoisie!='E')
							||(tronPlayer.direction=='E'&&directionChoisie!='W')
							||(tronPlayer.direction=='S'&&directionChoisie!='N')
							||(tronPlayer.direction=='N'&&directionChoisie!='S'))
						tronPlayer.direction = directionChoisie;
				}
				else tronServer.debutPartie=false;
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally // finally se produira le plus souvent lors de la deconnexion du client
		{
			try
			{
				
				// on indique a la console la deconnexion du client
				System.out.println("Player no."+tronPlayer.id+" waiting for disconnection.");
				this.tronPlayer.direction='X';
				while(tronPlayer.id!=1&&(tronServer.debutPartie||!tronServer.peutEtreDeconnecte))
				{
					this.sleep(500);
				}
				System.out.println("Player no."+tronPlayer.id+" disconnected.");
				tronServer.delClient(tronPlayer.id-1); // on supprime le client de la liste
				clientSocket.close(); // fermeture du socket si il ne l'a pas deja ete (a cause de l'exception levee plus haut)
			}
			catch (IOException e){ } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}



}
