package szolanc;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class TiltottDeploy {

	public TiltottDeploy(int serverDb) throws RemoteException {
		//System.out.println("Thank God it's working!");
        
        Registry reg = LocateRegistry.createRegistry(12345);
        
        for (int i = 1; i<=serverDb; ++i) {
            TiltottSzerver tServer = new TiltottSzerver(i);
            reg.rebind("tiltott"+i, tServer);
         //   System.out.println("Added to registry: tiltott" + i );
        }
	}

    public static void main(String[] args) throws RemoteException {
        
        int serverDb = Integer.parseInt(args[0]);
       
 
        
        if (args.length != 1 || serverDb<1  ) {
            System.out.println("Hibas parameterezes.");
            return;
        }

		new TiltottDeploy(serverDb);
        
        
        
       

    }

}
