package controller;

import dao.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Ensure user is logged in (double check, though AuthFilter handles it)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get current user
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // 2. Get form parameters
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        boolean hasError = false;
        
        // 3. Validate Inputs
        
        // Validate Current Password
        // Note: The user object in session contains the hashed password from login
        if (currentPassword == null || !BCrypt.checkpw(currentPassword, user.getPassword())) {
            request.setAttribute("errorCurrent", "Incorrect current password");
            hasError = true;
        }
        
        // Validate New Password Length
        if (newPassword == null || newPassword.length() < 8) {
            request.setAttribute("errorNew", "Password must be at least 8 characters");
            hasError = true;
        }
        
        // Validate Confirm Password
        if (confirmPassword == null || !confirmPassword.equals(newPassword)) {
            request.setAttribute("errorConfirm", "New passwords do not match");
            hasError = true;
        }
        
        // If validation failed, return to form with errors
        if (hasError) {
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }
        
        // 4. Update Password
        try {
            // Hash the new password
            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            
            // Update in database
            if (userDAO.updatePassword(user.getId(), newHashedPassword)) {
                // Update the user object in session with new password hash
                // This prevents "Incorrect current password" error if they try to change it again immediately
                user.setPassword(newHashedPassword);
                
                request.setAttribute("message", "Password changed successfully!");
            } else {
                request.setAttribute("error", "Database error. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred.");
        }
        
        request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
    }
}