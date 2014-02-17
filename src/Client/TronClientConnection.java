package Client;
import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.JLabel;


public class TronClientConnection extends Thread {
	
	int port = 2008;
    String address = "localhost";
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    String username = "Flexdec";
    Color color;
    TronClient client;
    char direction;
    //boolean debutPartie;
    Vector <TronPlayer> players;
    TronGrid grid;


    public TronClientConnection(TronClient client, TronGrid grid){
    	this.grid = grid;
    	this.port = client.serverport;
    	this.address = client.hostname;
    	address = "localhost";
    	this.client = client;
    	client.setPlayer(this);
    	players = client.players;
    	try {
    		socket = new Socket(address,port);
    		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		output = new PrintWriter(socket.getOutputStream());
    	}
    	catch (IOException e) {
    		e.printStackTrace();
      		System.exit(1);
      	}
    }
   
    synchronized public void run(){
    	try { // TRY PROTOCOLE
    		
    		client.sendUsername(this, username);
    		client.setGridSize(Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()));
    		
    		client.grid.dessinerMurEnceinte();
    		
    		
    		String ligneServeur;
    		int counter = 0;
    		while((ligneServeur = input.readLine())!=null)
    		{
    			if(ligneServeur.charAt(0)=='+')
    			{
    				String usernameFromServer = ligneServeur.substring(1);
    				String machineNameFromServer = input.readLine();
    				String RGBFromServer = input.readLine();
    				int posXFromServer = Integer.parseInt(input.readLine());
    				int posYFromServer = Integer.parseInt(input.readLine());
    				TronPlayer tronPlayer = new TronPlayer(usernameFromServer, machineNameFromServer, RGBFromServer,posXFromServer, posYFromServer);
    				System.out.println("User: "+usernameFromServer+"\nMachine: "+machineNameFromServer+"\nRGB: "+RGBFromServer+"\nPOS X: "+posXFromServer+"\nPOS Y: "+posYFromServer+"\n");
    				players.add(tronPlayer);
    				client.sideBar.addUserToSideBar(tronPlayer);
    				client.validate();
    			}
    			else if(ligneServeur.charAt(0)=='s')
    			{
    				System.out.println(ligneServeur);
    				String directionsDesJoueurs = ligneServeur.substring(1);
    				for(int i=0;i<players.size();i++){
    					players.elementAt(i).direction = directionsDesJoueurs.charAt(i);
    					players.elementAt(i).checkIfDead();
    				}
    				client.debutPartie = true;
    				grid.faireAvancerJoueurs();
    			}
    			else if(ligneServeur.charAt(0)=='R'){
    				this.sleep(5000);
    				client.clearGame();
    				client.validate();
    				players.clear();
    			}
    			
    			
    			
    		}

	    }
	    catch (IOException e) { // pour TRY PROTOCOLE
	      System.err.println("Exception: I/O error trying to talk to server: "
	                         + e);
	    } //catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
 catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
  
}
