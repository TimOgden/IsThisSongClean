/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import curses.CurseDatabase;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim
 */
public class Song {

    private String title, artist;

    public enum Cleanliness {
        CLEAN, MED, FOUL
    };
    private Cleanliness cleanliness = null;
    private UserAgent agent;
    private String lyricsSource;
    private double sexualInnuendos = 0;
    private double violence = 0;
    private double drugReferences = 0;
    private int timesVoted = 0;

    public Song(String title, String artist) throws FileNotFoundException {
        agent = new UserAgent();
        this.title = title;
        this.artist = artist;

    }

    public Song(String title, String artist, Cleanliness cleanliness) throws FileNotFoundException {
        agent = new UserAgent();
        this.title = title;
        this.artist = artist;
        this.cleanliness = cleanliness;

    }

    public Song(String title, String artist, String lyricsSource, int sexualInnuendos, int violence, int drugReferences, int timesVoted) {
        this.title = title;
        this.artist = artist;
        this.lyricsSource = lyricsSource;
        this.sexualInnuendos = sexualInnuendos;
        this.violence = violence;
        this.drugReferences = drugReferences;
        this.timesVoted = timesVoted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Cleanliness getCleanliness() {
        if (this.cleanliness == null) {
            try {
                return calculateCleanliness();
            } catch (ResponseException ex) {
                Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return this.cleanliness;
        }
        return null;
    }

    public void setCleanliness(Cleanliness cleanliness) {
        this.cleanliness = cleanliness;
    }

    public Cleanliness calculateCleanliness() throws ResponseException {
        if (agent == null) {
            agent = new UserAgent();
        }
        agent.visit("https://www.google.com");
        try {
            agent.doc.apply(title + " by " + artist + " lyrics");
            agent.doc.submit("Google Search");
        } catch (JauntException a) {

        }
        try {
            Element correction = agent.doc.findFirst("<a class=\"spell\">");
            String text = parseGoogleCorrection(correction);
        } catch (NotFound e) {

        }
        Elements element = agent.doc.findEvery("<a>");
        for (Element el : element) {
            String text = el.innerText().toLowerCase();
            if (text.contains(title.toLowerCase()) || text.contains(artist.toLowerCase())) {
                try {
                    //String loc = cleanUpURL(el.getAt("href"));
                    lyricsSource = cleanUpURL(el.getAt("href"));
                } catch (NotFound ex) {
                }
                agent.visit(lyricsSource);

                //System.out.println("LOCATION: " + loc);
                break;
            }

        }

        String text = agent.doc.innerText().toLowerCase();

        //if(!text.contains("lyric") && !text.contains("lyrics")&& !text.contains(artist) && !text.contains(title))
        //  return Cleanliness.NONEXISTENT;
        boolean isMed = false;
        Scanner scan = new Scanner(text);
        while (scan.hasNext()) {
            String next = scan.next();
            next = removePunctuation(next).toLowerCase();
            for (String curse : CurseDatabase.foulCurses) {
                if (next.equals(curse)) {
                    return Cleanliness.FOUL;
                }
            }
            for (String curse : CurseDatabase.medCurses) {
                if (next.equals(curse)) {
                    isMed = true;
                }
            }
        }
        if (isMed) {
            return Cleanliness.MED;
        } else {
            return Cleanliness.CLEAN;
        }
    }

    private String cleanUpURL(String url) {
        String ans = url.substring(29);
        int index = ans.indexOf("&");
        return ans.substring(0, index);
    }

    private String removePunctuation(String next) {
        String ans = "";
        for (int i = 0; i < next.length(); i++) {
            char c = next.charAt(i);
            if (Character.isAlphabetic(c)) {
                ans += c;
            }

        }
        return ans;
    }

    private String parseGoogleCorrection(Element e) {
        String temp = e.innerText();
        return temp.substring(0, temp.indexOf(" lyrics"));
    }

    public double getSexualInnuendos() {
        return sexualInnuendos;
    }

    public double getViolence() {
        return violence;
    }

    public double getDrugReferences() {
        return drugReferences;
    }

    public boolean isEqualTo(Song s1) {
        return s1.lyricsSource.equals(this.lyricsSource);
    }

    public String getLyricsSource() {
        return lyricsSource;
    }

    public int getTimesVoted() {
        return timesVoted;
    }

    public void setSexualInnuendos(double sexualInnuendos) {
        this.sexualInnuendos = sexualInnuendos;
    }

    public void setViolence(double violence) {
        this.violence = violence;
    }

    public void setDrugReferences(double drugReferences) {
        this.drugReferences = drugReferences;
    }

    public void incrementTimesVoted() {
        this.timesVoted++;
    }

    public void setTimesVoted(int timesVoted) {
        this.timesVoted = timesVoted;
    }

}
