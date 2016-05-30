package szolanc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class UserClient {

    UserClient() throws IOException {
        runGame();
    }

    private static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    private static void runGame() throws IOException {
        PrintWriter pw;
        BufferedReader br;
        Scanner sc = new Scanner(System.in);
        String answer;
        String word = "";
        List<String> words = new LinkedList<>();

        try {
            Socket client = new Socket("localhost", 32123);

            pw = new PrintWriter(client.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.err.println("Nem sikerult kapcsolodni a szerverhez.");
            return;
        }

        System.out.print("Nev: ");
        pw.println(sc.nextLine());

        answer = br.readLine();

        if ("start".equals(answer)) {
            System.out.print("Te kezdesz! Elso szo: ");
            word = sc.nextLine();
            words.add(word);
            pw.println(word);

            if (!"exit".equals(word)) {
                answer = br.readLine();
            } else {
                pw.close();
                br.close();
                sc.close();
            }
        }

        String lastReceivedWord = null;
        
        while (!"exit".equals(word)) {

            boolean ok = false;

            while (!ok) {
                if ("ok".equals(answer)) {
                    answer = br.readLine();
                    lastReceivedWord = answer;
                    ok = true;
                } else if ("nok".equals(answer)) {
                    System.out.println("Tiltott szo! Adj meg masikat.");

                    boolean correctWord = false;

                    while (!correctWord) {
                        System.out.print("Kovetkező szo: ");
                        word = sc.nextLine();
                        
  
                        if (!word.equals("") 
                                && (lastReceivedWord==null ? true : (word.charAt(0) == lastReceivedWord.charAt(lastReceivedWord.length() - 1))
                                || "exit".equals(word))
                                && isAlpha(word)) {

                            pw.println(word);
                            if ("exit".equals(word)) {
                                pw.close();
                                br.close();
                                sc.close();
                                ok = true;
                                break;
                            }

                            correctWord = true;
                            answer = br.readLine();
                        } else {
                            System.out.println("Helytelen szo!");
                        }

                    } 

                } else {  //első körben
                    lastReceivedWord = answer;
                    ok = true;
                }
            }

            if ("exit".equals(word)) {
                pw.close();
                br.close();
                sc.close();
                break;
            }

            if ("nyert".equals(answer)) {
                System.out.println("Te nyertel!");
                break;
            } else {
                System.out.println("Kapott szo: " + answer);
            }

            boolean correctWord = false;
            while (!correctWord) {
                System.out.print("Kovetkezo szo: ");
                word = sc.nextLine();

                if (!word.equals("") && (word.charAt(0) == answer.charAt(answer.length() - 1) || "exit".equals(word))
                        && isAlpha(word)) {

                    pw.println(word);
                    if ("exit".equals(word)) {
                        pw.close();
                        br.close();
                        sc.close();
                        break;
                    }

                    correctWord = true;
                    answer = br.readLine();
                } else {
                    System.out.println("Helytelen szo!");
                }

            }

            if (!correctWord) {
                break;
            }

        }

    }

    public static void main(String[] args) throws IOException {
        new UserClient();
    }

}
