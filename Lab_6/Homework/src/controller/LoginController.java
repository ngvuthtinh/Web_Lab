package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    /**
     * Display login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // If already logged in, redirect to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        
        // Show login page
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
    
    /**
     * Process login form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe"); // Đã sửa thành "rememberMe" cho khớp với JSP
        
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = userDAO.authenticate(username, password);
        
        if (user != null) {
            // Authentication successful
            
            // Invalidate old session (prevent session fixation)
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            
            // Create new session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("fullName", user.getFullName());
            
            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);
            
            // ============================================================
            // BONUS 5: TRACK VISITS & LAST LOGIN WITH COOKIES
            // ============================================================
            
            // 1. Calculate Visit Count
            int visitCount = 1;
            jakarta.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (jakarta.servlet.http.Cookie cookie : cookies) {
                    if ("visit_count".equals(cookie.getName())) {
                        try {
                            visitCount = Integer.parseInt(cookie.getValue()) + 1;
                        } catch (NumberFormatException e) {
                            visitCount = 1;
                        }
                        break;
                    }
                }
            }

            // 2. Save Updated Visit Count (Expires in 1 year)
            jakarta.servlet.http.Cookie visitCookie = new jakarta.servlet.http.Cookie("visit_count", String.valueOf(visitCount));
            visitCookie.setMaxAge(365 * 24 * 60 * 60);
            visitCookie.setPath("/");
            response.addCookie(visitCookie);

            // 3. Save Last Login Timestamp (Expires in 1 year)
            jakarta.servlet.http.Cookie lastLoginCookie = new jakarta.servlet.http.Cookie("last_login", String.valueOf(System.currentTimeMillis()));
            lastLoginCookie.setMaxAge(365 * 24 * 60 * 60);
            lastLoginCookie.setPath("/");
            response.addCookie(lastLoginCookie);
            
            // ============================================================
            // END BONUS 5
            // ============================================================
            
            // Handle "Remember Me" (BONUS 2)
            if ("on".equals(rememberMe)) {
                // 1. Generate secure random token
                String token = java.util.UUID.randomUUID().toString();
                
                // 2. Save token to database
                userDAO.saveRememberToken(user.getId(), token);
                
                // 3. Create secure cookie
                jakarta.servlet.http.Cookie rememberCookie = new jakarta.servlet.http.Cookie("remember_token", token);
                rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                rememberCookie.setPath("/"); // Available for whole app
                rememberCookie.setHttpOnly(true); // Security
                response.addCookie(rememberCookie);
            }
            
            // Redirect based on role
            if (user.isAdmin()) {
                response.sendRedirect("dashboard");
            } else {
                response.sendRedirect("student?action=list");
            }
            
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username); // Keep username in form
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}