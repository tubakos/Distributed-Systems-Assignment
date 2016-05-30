/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package szolanc;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface TiltottSzerverI extends Remote{
    
    public boolean tiltottE(String szo) throws RemoteException;
    
}
