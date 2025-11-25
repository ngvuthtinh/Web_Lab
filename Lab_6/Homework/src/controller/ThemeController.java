package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/theme")
public class ThemeController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String theme = request.getParameter("mode");
        
        // Validate theme value
        if ("light".equals(theme) || "dark".equals(theme)) {
            // Save preference in cookie for 1 year
            Cookie themeCookie = new Cookie("user_theme", theme);
            themeCookie.setMaxAge(365 * 24 * 60 * 60); // 1 year
            themeCookie.setPath("/");
            // Allow JavaScript access if needed, but not strictly required here
            themeCookie.setHttpOnly(false); 
            response.addCookie(themeCookie);
        }
        
        // Redirect back to the page user came from (Reload page)
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("dashboard");
        }
    }
}
