package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class PlayerStandBy extends Thread{
	TronServer tronServer;
	Socket clientSocket;;
	boolean go;
 public PlayerStandBy(Socket clientSocket, TronServer tronServer)
 {
	 go = false;
	 this.clientSocket = clientSocket;
	 this.tronServer = tronServer;
 }

 public void run(){
	 try {
		 while(!go){
			 this.sleep(500);
			 if(tronServer.joueurPeutConnecter) go=true;
		 }
		 this.sleep(6000);
	 } catch (InterruptedException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	 }
	 
	 PlayerConnection player = new PlayerConnection(clientSocket, tronServer);
	 player.start();
 }
}

