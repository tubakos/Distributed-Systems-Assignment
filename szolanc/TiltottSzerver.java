/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package szolanc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;


public class TiltottSzerver  extends java.rmi.server.UnicastRemoteObject
    implements TiltottSzerverI {

    int id;
    List<String> words = new LinkedList();
    Reader file;

    public TiltottSzerver(int id) throws RemoteException {
        super();
        this.id = id;
        readFromFile(id);
        //System.out.println(words);
    }

    private void readFromFile(int id) {
        String fileName = "tiltott" + id + ".txt";

        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            System.err.println("Fajl nem talalhato.");
            return;
        }

        try {
            BufferedReader fileBr = new BufferedReader(file);
            while (fileBr.ready()) {
                words.add(fileBr.readLine());
            }
            fileBr.close();
            file.close();
        } catch (IOException ex) {
            System.err.println("Hiba a beolvasas soran.");
        }

    }

    @Override
    public boolean tiltottE(String szo) throws RemoteException {
       boolean contains = words.contains(szo);
       words.add(szo);
       return contains;
    }

}
