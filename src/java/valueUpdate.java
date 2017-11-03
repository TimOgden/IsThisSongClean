/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import music.Song;
import music.SongDatabase;

/**
 *
 * @author Tim
 */
@WebServlet(name = "ValueUpdate", urlPatterns = {"/valueUpdate"})
public class valueUpdate extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String title = request.getParameter("title");
        String artist = request.getParameter("artist");
        String lyricsSource = request.getParameter("lyricsSource");
        double drugReferences = Double.parseDouble(request.getParameter("drugReferences"));
        double violence = Double.parseDouble(request.getParameter("violence"));
        double sexualInnuendos = Double.parseDouble(request.getParameter("sexualInnuendos"));
        System.out.println("Before:");
        System.out.println("Drugs: " + drugReferences);
        System.out.println("Violence: " + violence);
        System.out.println("Sex: " + sexualInnuendos);
        Song s = SongDatabase.getSong(lyricsSource);
        s.incrementTimesVoted();
        s.setDrugReferences((s.getDrugReferences() + drugReferences)/s.getTimesVoted());
        s.setSexualInnuendos((s.getSexualInnuendos()+ sexualInnuendos)/s.getTimesVoted());
        s.setViolence((s.getViolence()+ violence)/s.getTimesVoted());
        System.out.println("After:");
        System.out.println("Drugs: " + s.getDrugReferences());
        System.out.println("Violence: " + s.getViolence());
        System.out.println("Sex: " + s.getSexualInnuendos());
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet valueUpdate</title>"); 
            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet valueUpdate at " + request.getContextPath() + "</h1>");
            for(Song song : SongDatabase.getSongList())
                out.println("<h2>" + song.getTitle() + " by " + song.getArtist() + "</h2>");
            out.println("<h3>Hidden lyricsSource parameter: " + lyricsSource + "</h3>");
            out.println("<h3>Thank you for your input!</h3>");
            out.println("<h3>The value of timesVoted for this song is now " + s.getTimesVoted() + "</h3>");
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
