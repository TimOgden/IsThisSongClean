/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jaunt.Element;
import com.jaunt.JauntException;
import curses.CurseDatabase;
import exceptions.SongNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import music.Song;
import music.Song.Cleanliness;
import music.SongDatabase;

/**
 *
 * @author Tim
 */
@WebServlet(urlPatterns = {"/songServlet"})
public class SongForm extends HttpServlet {

    public static CurseDatabase database;
    private static int timesUsed = 0;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JauntException {

        response.setContentType("text/html;charset=UTF-8");
        if(SongDatabase.getSongList().size()==0)
            if(SongDatabase.shouldLoadOnStart())
                SongDatabase.loadSongs();
        timesUsed++;
        if (timesUsed % 50 == 0) {
            SongDatabase.saveSongs();
        }

        CurseDatabase.initDatabase();
        String title = request.getParameter("Title");
        String artist = request.getParameter("Artists");
        title = fixStrings(title);
        artist = fixStrings(artist);
        Song s = new Song(title, artist);
        
        //The bug most likely lies somewhere from here
        s.calculateCleanliness();
        Song s1 = null;
        
        try {
            s1 = SongDatabase.checkDatabase(s);
            //s.setCleanliness(getGreaterCleanliness(s1.getCleanliness(), s.getCleanliness()));
            //s.setTimesVoted(s1.getTimesVoted());
        } catch (SongNotFoundException snfe) {
            s.calculateCleanliness();
        }
        if (!SongDatabase.songAlreadyExists(s)) {
            SongDatabase.addSong(s);
        }
        //to here.
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title id=\"title\">");
            out.println(title + ": " + s.getCleanliness().name());
            out.println("</title>");
            out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css\" "
                    + "integrity=\"sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb\" crossorigin=\"anonymous\">");
            out.println("<link rel=\"icon\" href=\"favicon.ico\">\n"
                    + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"./css/css.css\">");
            out.println("<script>"
                    + "function showSliders() {"
                    + " document.getElementById(\"rankingSliders\").style.display = 'block';"
                    + "}"
                    + "</script>\n"
                    
                    + "<script>"
                    + "function showExplanation() {"
                    + "     document.getElementById(\"buttonExplanation\").style.display = 'block';"
                    + "}</script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"title\"><h1 id=\"title\">Is This Song Clean?</h1></div>");
            out.println("<div class=\"result\">");
            out.println("<h1>" + title + " by " + artist + "</h1>");
            out.println("<h2>" + s.getCleanliness().name() + "</h2>");
            out.println("</div>");
            //out.println("<h5>" + c.toString() + "</h5>");
            boolean temp = false;
            if(s1!=null)
                temp = s1.getTimesVoted()>0;
            if (temp) {
                int drug = (int)Math.round(s1.getDrugReferences());
                int violence = (int)Math.round(s1.getViolence());
                int sex = (int)Math.round(s1.getSexualInnuendos());
                out.println("<h3>Drug/Alcohol References:</h3>");
                out.println("<h4 id=\"ratingVal\" style=\"color: ");
                if(drug == 0)
                    out.print("green");
                else if(drug>0 && drug<=6)
                    out.print("orange");
                else if(drug>6)
                    out.print("red");
                out.print("\">" + drug + "/10</h4>");

                out.println("<h3>Sexual Innuendos:</h3>");
                out.println("<h4 id=\"ratingVal\" style=\"color: ");
                if(sex == 0)
                    out.print("green");
                else if(sex>0 && sex<=6)
                    out.print("orange");
                else if(sex>6)
                    out.print("red");
                out.print("\">" + sex + "/10</h4>");

                out.println("<h3>Violence References:</h3>");
                out.println("<h4 id=\"ratingVal\" style=\"color: ");
                if(violence == 0)
                    out.print("green");
                else if(violence>0 && violence<=6)
                    out.print("orange");
                else if(violence>6)
                    out.print("red");
                out.print("\">" + violence + "/10</h4>");

                out.println("<h4>Average of <b>" + s1.getTimesVoted() + "</b> user submissions. Help improve it by submitting your own down below!</h4>");
            } else {
                
                out.println("<div id=\"resultsUnavailable\">\n"
                        + "<h4>Not enough people have voted on this song to gather more information on its explicitness. Be the first down below!</h4>\n"
                        + "</div>");
            }
            out.println("<h4>Source: " + s.getLyricsSource() + "</h4>");
            out.println("<a href=\"http://isthissongclean.com\">Search another song</a>");
            out.println("<br>");
            out.println("<br>");
            out.println("<button class=\"helpButton\" onClick=\"showSliders()\" id=\"showButton\">Help make this more accurate!</button>"); //showSliders()
            out.println("<div id=\"rankingSliders\" class=\"rankingSliders\">");
            out.println("<form name=\"values\" method=\"post\" action=\"valueUpdate\">"); //values, valueUpdate
            out.println("<h3>Drug/Alcohol References</h3>");
            out.println("<input name=\"drugReferences\" type=\"range\" min=0 max=10 value=0>");
            out.println("<h3>Sexual Innuendos</h3>");
            out.println("<input name=\"sexualInnuendos\" type=\"range\" min=0 max=10 value=0>");
            out.println("<h3>Violence References</h3>");
            out.println("<input name=\"violence\" type=\"range\" min=0 max=10 value=0>");
            out.println("<input type=\"hidden\" value=\"" + s.getTitle() + "\" name=\"title\">");
            out.println("<input type=\"hidden\" value=\"" + s.getArtist() + "\" name=\"artist\">");
            out.println("<input type=\"hidden\" value=\"" + s.getLyricsSource() + "\" name=\"lyricsSource\">");
            out.println("<br>");
            out.println("<button type=\"submit\">Submit Feedback</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<h4>What is this?<input type=\"image\" src=\"./q.png\" height=16 width=16 onClick=\"showExplanation()\"/></h4>"); //showExplanation
            out.println("<div class=\"explanation\" id=\"buttonExplanation\">");
            out.println("<h4 id=\"buttonWExplanation\">While the IsThisSongClean web bot is great at finding curse words in songs, it's not that great at implied innapropriate things"
                    + " such as innuendos. That's why all users on IsThisSongClean can rate different fields of explicitness for an extra degree of accuracy.</h4>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div id=\"footer\" class=\"bottom\">\n" +
"                <div id=\"footer-content\">\n" +
"                    <h4>My name is Tim, and I created IsThisSongClean. I am a freshman in college, and if you appreciate this application and want to help keep it a free service, a small donation would be greatly appreciated. Thank you!</h4>\n" +
"                    <form action=\"https://www.paypal.com/cgi-bin/webscr\" method=\"post\" target=\"_top\">\n" +
"                        <input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">\n" +
"                        <input type=\"hidden\" name=\"encrypted\" value=\"-----BEGIN PKCS7-----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-----END PKCS7-----\n" +
"                               \">\n" +
"                        <input type=\"image\" src=\"https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif\" border=\"0\" name=\"submit\" alt=\"PayPal - The safer, easier way to pay online!\">\n" +
"                        <img alt=\"\" border=\"0\" src=\"https://www.paypalobjects.com/en_US/i/scr/pixel.gif\" width=\"1\" height=\"1\">\n" +
"\n" +
"\n" +
"                    </form>\n" +
"                    <div id=\"contactMe\" class=\"contact\">\n" +
"                        <a href=\"mailto:togden1999@gmail.com\">Contact me for suggestions and comments</a>\n" +
"                    </div>\n" +
"                </div>\n" +
"            </div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            //SongDatabase.saveSongs();
        }
    }

    private String fixStrings(String s) {
        String ans = "";
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                ans += Character.toUpperCase(s.charAt(i));
            } else {
                ans += Character.toLowerCase(s.charAt(i));
            }
        }
        return ans;
    }

    private Cleanliness getGreaterCleanliness(Cleanliness c1, Cleanliness c2) {
        int ans = c1.compareTo(c2);
        if (ans == -1) {
            return c2;
        }
        if (ans == 0) {
            return c1;
        }
        if (ans == 1) {
            return c1;
        }
        return c1;
    }

    private int getGreaterTimesVoted(Song s, Song s1) {
        System.out.println("TimesVoted: " + s.getTimesVoted() + " " + s1.getTimesVoted());
        if (s.getTimesVoted() > s1.getTimesVoted()) {
            return s.getTimesVoted();
        } else {
            return s1.getTimesVoted();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JauntException ex) {
            Logger.getLogger(SongForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JauntException ex) {
            Logger.getLogger(SongForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
