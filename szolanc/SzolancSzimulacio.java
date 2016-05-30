package szolanc;


import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SzolancSzimulacio {

    public static void main(String[] args) throws IOException {
		
		new TiltottDeploy(2);
		
        new Thread() {
            @Override
            public void run() {
                try {
                    new Server();
                } catch (IOException e) {
                    System.err.println("Szerver oldali hiba.");
                } catch (NotBoundException ex) {
                    Logger.getLogger(SzolancSzimulacio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new ComputerClient("Jatekos1", "szokincs1.txt");
                } catch (IOException e) {
                    System.err.println("Kliens oldali hiba.");
                }
            }
        }.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(SzolancSzimulacio.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    new ComputerClient("Jatekos2", "szokincs1.txt");
                } catch (IOException e) {
                    System.err.println("Kliens oldali hiba.");
                }
            }
        }.start();
         try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(SzolancSzimulacio.class.getName()).log(Level.SEVERE, null, ex);
        }


        new Thread() {
            @Override
            public void run() {
                try {
                    new ComputerClient("Jatekos3", "szokincs1.txt");
                } catch (IOException e) {
                    System.err.println("Kliens oldali hiba.");
                }
            }
        }.start();
         try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(SzolancSzimulacio.class.getName()).log(Level.SEVERE, null, ex);
        }

        
         new Thread() {
            @Override
            public void run() {
                try {
                    new ComputerClient("Jatekos4", "szokincs2.txt");
                } catch (IOException e) {
                    System.err.println("Kliens oldali hiba.");
                }
            }
        }.start();

       

    }

}
