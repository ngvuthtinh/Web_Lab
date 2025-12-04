package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication Filter - Checks if user is logged in
 * Protects all pages except login and public resources
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    
    // Public URLs that don't require authentication
    private static final String[] PUBLIC_URLS = {
        "/login",
        "/logout",
        ".css",
        ".js",
        ".png",
        ".jpg",
        ".jpeg",
        ".gif"
    };
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // 1. Allow public URLs
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 2. Check Session
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        
        // 3. AUTO-LOGIN Logic (If not logged in, check cookie)
        if (!isLoggedIn) {
            String token = null;
            jakarta.servlet.http.Cookie[] cookies = httpRequest.getCookies();
            
            if (cookies != null) {
                for (jakarta.servlet.http.Cookie cookie : cookies) {
                    if ("remember_token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            
            if (token != null) {
                // Check DB for token
                dao.UserDAO userDAO = new dao.UserDAO();
                model.User user = userDAO.getUserByToken(token);
                
                if (user != null) {
                    // Token valid -> Auto create session
                    session = httpRequest.getSession(true);
                    session.setAttribute("user", user);
                    session.setAttribute("role", user.getRole());
                    session.setAttribute("fullName", user.getFullName());
                    isLoggedIn = true; // Mark as logged in
                } else {
                    // Token invalid -> Delete cookie
                    jakarta.servlet.http.Cookie deleteCookie = new jakarta.servlet.http.Cookie("remember_token", "");
                    deleteCookie.setMaxAge(0);
                    deleteCookie.setPath("/");
                    httpResponse.addCookie(deleteCookie);
                }
            }
        }
        
        // 4. Final Check
        if (isLoggedIn) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
    
    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
    }
    
    /**
     * Check if URL is public (doesn't require authentication)
     */
    private boolean isPublicUrl(String path) {
        for (String publicUrl : PUBLIC_URLS) {
            if (path.contains(publicUrl)) {
                return true;
            }
        }
        return false;
    }
}
