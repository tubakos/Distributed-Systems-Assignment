package szolanc;


import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SzolancJatek {

    public static void main(String[] args) throws IOException, InterruptedException {
		
		new TiltottDeploy(1);

        new Thread() {
            @Override
            public void run() {
                try {
                    Server.main(new String[0]);
                    
                } catch (IOException e) {
                    System.err.println("Szerver oldali hiba.");
                } catch (NotBoundException ex) {
                    Logger.getLogger(SzolancJatek.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        
      
        
          new Thread() {
            @Override
            public void run() {
                try {
                    new ComputerClient("Robot", "szokincs1.txt");
                } catch (IOException e) {
                    System.err.println("Kliens oldali hiba.");
                
                }
            }
        }.start();
          
       

        new Thread() {
            @Override
            public void run() {
                try {
                    new UserClient();
                } catch (IOException e) {
                    System.err.println("Kliens oldali hiba.");
                }
            }
        }.start();

      

    }
}
