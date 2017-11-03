/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import java.util.ArrayList;
import exceptions.SongNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import music.Song.Cleanliness;

/**
 *
 * @author Tim
 */
public class SongDatabase {

    private static ArrayList<Song> songList = new ArrayList<>();

    private static final boolean LOAD_ON_START = true;
    
    public static boolean shouldLoadOnStart() {
        return LOAD_ON_START;
    }
    public static void addSong(Song s) {
        songList.add(s);
    }

    public static ArrayList<Song> getSongList() {
        return songList;
    }
    public static boolean songAlreadyExists(Song s) {
        for(Song s1 : songList) {
            if(s1.isEqualTo(s))
                return true;
        }
        return false;
    }
    public static Song checkDatabase(Song s) throws SongNotFoundException {
        //TODO: Improve this search function to make it faster
        for (Song s1 : songList) {
            if (s.isEqualTo(s1)) {
                //System.out.println("s1: " + s1.getTimesVoted());
                //System.out.println("s: " + s.getTimesVoted());
                return s1;
            }
        }
        throw new SongNotFoundException();
    }

    public static void clearDuplicates() {
        
        for(int i = 0; i<songList.size(); i++) {
            for(int k = 0; k<songList.size(); k++) {
                if(i!=k) {
                    Song songI = songList.get(i);
                    Song songK = songList.get(k);
                    if(songI.isEqualTo(songK)) {
                        if(songI.getTimesVoted()<songK.getTimesVoted())
                            songList.remove(songI);
                        else
                            songList.remove(songK);
                    }
                    
                }
            }
        }
    }
    public static void saveSongs() {
        File f = new File("songs.txt");
        BufferedWriter bf;
        try {
            bf = new BufferedWriter(new FileWriter(f));
            
            for(int i = 0; i<songList.size(); i++) {
                Song s = songList.get(i);
                if(i!=songList.size()-1) {
                    bf.write(s.getTitle() + "\n" + s.getArtist() + "\n" + s.getLyricsSource() + "\n" + s.getSexualInnuendos() + 
                            "\n" + s.getViolence() + "\n" + s.getDrugReferences() + "\n" + s.getTimesVoted() + "\n");
                } else {
                    bf.write(s.getTitle() + "\n" + s.getArtist() + "\n" + s.getLyricsSource()
                     + s.getSexualInnuendos() + 
                            "\n" + s.getViolence() + "\n" + s.getDrugReferences() + "\n" + s.getTimesVoted());
                }
            }
            bf.close();
        } catch (IOException ex) {
            Logger.getLogger(SongDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void loadSongs() throws FileNotFoundException {
        //TODO: Create a way to call this function
        Scanner scan = new Scanner(new File("songs.txt"));
        songList = new ArrayList<>();
        while(scan.hasNextLine()) {
            String s1 = scan.nextLine();
            if(!scan.hasNextLine())
                break;
            String s2 = scan.nextLine();
            String s3 = scan.nextLine();
            int sexualInnuendos = (int)Double.parseDouble(scan.nextLine());
            int violence = (int)Double.parseDouble(scan.nextLine());
            int drugReferences = (int)Double.parseDouble(scan.nextLine());
            int timesVoted = (int)Double.parseDouble(scan.nextLine());
            
            songList.add(new Song(s1,s2,s3, sexualInnuendos, violence, drugReferences, timesVoted));
        }
    }

    public static Song getSong(String lyricsSource) {
        for(Song s : songList) {
            if(lyricsSource.equals(s.getLyricsSource()))
                return s;
        }
        return null;
    }

}
