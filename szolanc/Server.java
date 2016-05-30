package szolanc;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static int PORT = 32123;

    public Server() throws IOException, RemoteException, NotBoundException {
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Szerver elindult.");

        Socket player1;
        Socket player2;

        ss.setSoTimeout(30000);

        while (true) {
            try {
                player1 = ss.accept();
                player2 = ss.accept();

                new Gameplay(player1, player2).start();
            } catch (SocketTimeoutException e) {
                break;
            }

        }
    }

    private static String askName(Socket s) throws IOException {
        String name;
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        name = br.readLine();

        return name;
    }

    public static void main(String[] args) throws IOException, RemoteException, NotBoundException {
        new Server();
    }

    static class Gameplay extends Thread {

        static int gameID = 1;

        Socket player1;
        Socket player2;
        String name1;
        String name2;

        TiltottSzerverI tServer;

        Writer writer;
        PrintWriter pw1;
        PrintWriter pw2;
        BufferedReader br1;
        BufferedReader br2;

        public Gameplay(Socket player1, Socket player2) throws IOException, RemoteException, NotBoundException {
            this.player1 = player1;
            this.player2 = player2;
            
           

            try {
                tServer = (TiltottSzerverI) Naming.lookup("rmi://localhost:12345/tiltott" + gameID);
                gameID++;
            } catch (Exception e) {
                gameID = 1;
                tServer = (TiltottSzerverI) Naming.lookup("rmi://localhost:12345/tiltott" + gameID);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void endGame() throws IOException {
            pw1.close();
            pw2.close();
            br1.close();
            br2.close();
            writer.close();
        }

        @Override
        public void run() {

            try {
                name1 = askName(player1);
                name2 = askName(player2);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            Date dNow = new Date();
            SimpleDateFormat ft
                    = new SimpleDateFormat("yyyy.MM.dd_HHmmss");

            try {
                writer = new FileWriter(name1 + "_" + name2 + "_" + ft.format(dNow) + ".txt");
                pw1 = new PrintWriter(player1.getOutputStream(), true);
                pw2 = new PrintWriter(player2.getOutputStream(), true);
                br1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                br2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            pw1.println("start");
            boolean firstPlayer = true;
            String word;

            while (true) {
                if (firstPlayer) {
                    try {
                        word = br1.readLine();

                        if (word.equals("exit")) {
                            pw2.println("nyert");
                            endGame();
                            break;
                        } else if (tServer.tiltottE(word)) {
                            pw1.println("nok");
                          //  pw1.flush();
                        } else {
                            pw1.println("ok");
                          //  pw1.flush();
                            writer.write(name1 + " " + word);
                            writer.write(System.lineSeparator());
                            writer.flush();
                            pw2.println(word);
                            firstPlayer = false;
                        }
                    } catch (IOException ex) {
                        System.err.println("Kommunikacios problema egy kliensnel. Nev: " + name1);

                    }
                } else {
                    try {
                        word = br2.readLine();

                        if (word.equals("exit")) {
                            pw1.println("nyert");
                            endGame();
                            break;
                        } else if (tServer.tiltottE(word)) {
                            pw2.println("nok");
                           // pw2.flush();
                        } else {
                            pw2.println("ok");
                            //pw2.flush();
                            writer.write(name2 + " " + word);
                            writer.write(System.lineSeparator());
                            writer.flush();
                            pw1.println(word);
                            firstPlayer = true;
                        }
                    } catch (IOException ex) {
                        System.err.println("Kommunikacios problema egy kliensnel. Nev: " + name2);

                    }
                }

            }

        }
    }
}
