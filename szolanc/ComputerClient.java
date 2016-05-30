package szolanc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ComputerClient {

    public ComputerClient(String name, String filename) throws IOException {
        runGame(name, filename);
    }

    private static void runGame(String name, String filename) throws IOException {

        PrintWriter pw;
        BufferedReader br;
        String answer;
        String word = "";
        LinkedList<String> words = new LinkedList<>();
        Reader file;

        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException ex) {
            System.err.println("Fajl nem talalhato.");
            return;
        }

        //szókincs beolvasása
        BufferedReader fileBr = new BufferedReader(file);
        while (fileBr.ready()) {
            words.add(fileBr.readLine());
        }
        fileBr.close();
        file.close();

        try {
            Socket client = new Socket("localhost", 32123);

            pw = new PrintWriter(client.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.err.println("Nem sikerult kapcsolodni a szerverhez.");
            return;
        }

        //név elküldése
        pw.println(name);

        String lastReceivedWord = null;
        answer = br.readLine();

        if ("start".equals(answer)) {
            // System.out.print("Te kezdesz! Elso szo: ");

            if (!words.isEmpty()) {
                pw.println(word = words.getFirst());
                answer = br.readLine();
            } else {
                word = "exit";
                pw.println(word);
                pw.close();
                br.close();
            }
        } else {
            lastReceivedWord = answer;
        }

        
        while (!"exit".equals(word)) {
            boolean ok = false;

            
            while (!ok) {

                if ("nok".equals(answer)) {
                  
                    words.remove(word);
                  
                    boolean correctWord = false;
                    int index = 0;

                    while (index < words.size() && !correctWord && !words.isEmpty()) {
                        word = words.get(index++);
                        
                        if (lastReceivedWord == null) {
                            pw.println(word);
                            correctWord = true;
                            answer = br.readLine();
                        }
                        if ( lastReceivedWord != null && word.charAt(0) == lastReceivedWord.charAt(lastReceivedWord.length() - 1)) {
                            pw.println(word);
                            correctWord = true;
                            answer = br.readLine();
                        }
                    }

                    if (!correctWord) {
                        word = "exit";
                        ok = true;
                        pw.println(word);
                        pw.close();
                        br.close();
                    }

                } else if ("ok".equals(answer)) {
                   System.out.println(name + ": " + word);
                    ok = true;
                    answer = br.readLine();
                    lastReceivedWord = answer;
                    words.remove(word);
                
                } else {  //első körben
                    ok = true;
                }
            }
            
            
            if("exit".equals(word)) {
                break;
            }

            if ("nyert".equals(answer)) {
                System.out.println(name + " nyert");
                break;
            }

            boolean correctWord = false;
            int index = 0;
            while (index < words.size() && !correctWord && !words.isEmpty() ) {
                word = words.get(index++);
                if (word.charAt(0) == lastReceivedWord.charAt(lastReceivedWord.length() - 1)) {    //!!!!!!!
                    pw.println(word);
                    correctWord = true;
                    answer = br.readLine();
                }
            }

            if (!correctWord) {
                word = "exit";
                pw.println(word);
                pw.close();
                br.close();
            }

        }

    }

    public static void main(String[] args) throws IOException {
        new ComputerClient(args[0], args[1]);
    }
}
