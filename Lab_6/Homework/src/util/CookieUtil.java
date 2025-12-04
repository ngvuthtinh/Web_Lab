package util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    
    /**
     * Create and add cookie to response
     * @param response HTTP response
     * @param name Cookie name
     * @param value Cookie value
     * @param maxAge Cookie lifetime in seconds
     */
    public static void createCookie(HttpServletResponse response, 
                                   String name, 
                                   String value, 
                                   int maxAge) {
        // 1. Create new Cookie with name and value
        Cookie cookie = new Cookie(name, value);
        
        // 2. Set maxAge (in seconds)
        cookie.setMaxAge(maxAge);
        
        // 3. Set path to "/" (Available for entire application)
        cookie.setPath("/");
        
        // 4. Set httpOnly to true (Security: prevents JavaScript access)
        cookie.setHttpOnly(true);
        
        // 5. Add cookie to response
        response.addCookie(cookie);
    }
    
    /**
     * Get cookie value by name
     * @param request HTTP request
     * @param name Cookie name
     * @return Cookie value or null if not found
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        // 1. Get all cookies from request
        Cookie[] cookies = request.getCookies();
        
        // 2. Check if cookies array is null (important validation)
        if (cookies != null) {
            // 3. Loop through cookies
            for (Cookie cookie : cookies) {
                // 4. Find cookie with matching name
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    /**
     * Check if cookie exists
     * @param request HTTP request
     * @param name Cookie name
     * @return true if cookie exists
     */
    public static boolean hasCookie(HttpServletRequest request, String name) {
        // Reuse getCookieValue logic
        return getCookieValue(request, name) != null;
    }
    
    /**
     * Delete cookie by setting max age to 0
     * @param response HTTP response
     * @param name Cookie name to delete
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        // 1. Create cookie with same name and empty value
        Cookie cookie = new Cookie(name, "");
        
        // 2. Set maxAge to 0 (Tells browser to delete it immediately)
        cookie.setMaxAge(0);
        
        // 3. Set path to "/" (Must match the original cookie's path to delete successfully)
        cookie.setPath("/");
        
        // 4. Add to response
        response.addCookie(cookie);
    }
    
    /**
     * Update existing cookie
     * @param response HTTP response
     * @param name Cookie name
     * @param newValue New cookie value
     * @param maxAge New max age
     */
    public static void updateCookie(HttpServletResponse response, 
                                   String name, 
                                   String newValue, 
                                   int maxAge) {
        // Simply create a new cookie with same name to overwrite the old one
        createCookie(response, name, newValue, maxAge);
    }
}