/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Tim
 */
public class CurseDatabase {

    public static ArrayList<String> foulCurses;
    public static ArrayList<String> medCurses;
    private static File f;
    private static boolean isInitialized = false;

    public static void initDatabase() throws FileNotFoundException {
        if (isInitialized) {
            return;
        }
        f = new File("//opt/tomcat/apache-tomcat-9.0.0.M26/webapps/IsThisSongClean/foulWords.csv");
        Scanner scan = new Scanner(f);
        scan.useDelimiter(",");
        foulCurses = new ArrayList<>();
        while (scan.hasNext()) {
            String next = scan.next();
            
            if (!next.trim().equals("")) {
                foulCurses.add(next);
                //System.out.println(next);
            }
        }

        f = new File("//opt/tomcat/apache-tomcat-9.0.0.M26/webapps/IsThisSongClean/medWords.csv");
        scan = new Scanner(f);
        scan.useDelimiter(",");
        medCurses = new ArrayList<>();
        while (scan.hasNext()) {
            String next = scan.next();
            
            if (!next.trim().equals("")) {
                medCurses.add(next);
                //System.out.println(next);
            }
        }
        isInitialized = true;
    }
}
