package Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TronHeartBeat extends Thread 
{
	TronServer tronServer;
	Timer t;
	public TronHeartBeat(TronServer tronServer)
	{
		this.tronServer = tronServer;
		t = new Timer();
		this.start();
	}
	public void run()
	{
		try 
		{
			while(tronServer.tabDeClients.size()<2){
				this.sleep(1000);	
			}
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		LancerLaPartie lancer = new LancerLaPartie();
		t.schedule(lancer, 10000);
		
	}
	class LancerLaPartie extends TimerTask 
	{
		public void  run() 
		{
			tronServer.joueurPeutConnecter = false;
			tronServer.peutEtreDeconnecte=true;
			tronServer.debutPartie=true;
			
			t.schedule(new EnvoyerDirections(), 0, tronServer.clockTick);
			this.cancel();
		}
	}
	class EnvoyerDirections extends TimerTask 
	{
		public void  run() 
		{
			int counter = 0;
			if(!tronServer.debutPartie) this.cancel();
			String directionsDesJoueurs = "s";
			for(int i=0;i<tronServer.tabDeClients.size();i++)
			{
				PlayerConnection pc = (PlayerConnection) tronServer.tabDeClients.elementAt(i);
				directionsDesJoueurs += pc.tronPlayer.direction;
				if(pc.tronPlayer.direction=='X')
				{
					counter++;
				}
			}
			if(counter>=tronServer.tabDeClients.size()-1)
			{
				tronServer.debutPartie=false;
				t.schedule(new RecommencerLaGame(), 10000);
				this.cancel();
			}
			tronServer.FaireAvancerLesJoueurs();
			tronServer.sendAll(directionsDesJoueurs);
			if(!tronServer.debutPartie){
				tronServer.sendAll("R");
			}
		}
	}
	class RecommencerLaGame extends TimerTask
	{
		public void run()
		{
			tronServer.peutEtreDeconnecte=false;
			for(int i=0;i<tronServer.tabDeClients.size();i++)
			{
				tronServer.tabDeClients.elementAt(i).tronPlayer = new TronPlayer(
						tronServer.tabDeClients.elementAt(i).tronPlayer, 
						tronServer.setInitialPosition(),		
						tronServer.setInitialDirection());			
			}
			
			tronServer.sendAllPlayersPositions();
			tronServer.joueurPeutConnecter = true;
			tronServer.array = new int[tronServer.gridWidth][tronServer.gridHeight];
			tronServer.DessinerMurEnceinte();
			TronHeartBeat.this.run();
		}
	}
	

}