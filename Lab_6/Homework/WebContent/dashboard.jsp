<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    // Lấy theme từ Cookie (Mặc định là 'light')
    String currentTheme = "light";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("user_theme".equals(cookie.getName())) {
                currentTheme = cookie.getValue();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html data-theme="<%= currentTheme %>">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        /* --- 1. DEFINING THEME VARIABLES --- */
        :root {
            /* Default (Light) Theme Colors */
            --bg-body: #f5f5f5;
            --bg-navbar: #2c3e50;
            --text-navbar: white;
            --bg-card: white;
            --text-main: #2c3e50;
            --text-secondary: #7f8c8d;
            --shadow: rgba(0,0,0,0.1);
            --btn-hover-bg: rgba(255,255,255,0.1);
        }

        /* Dark Theme Overrides */
        [data-theme="dark"] {
            --bg-body: #1a1a1a;
            --bg-navbar: #000000;
            --text-navbar: #e0e0e0;
            --bg-card: #2d2d2d;
            --text-main: #ecf0f1;
            --text-secondary: #bdc3c7;
            --shadow: rgba(0,0,0,0.5);
            --btn-hover-bg: rgba(255,255,255,0.2);
        }

        /* --- 2. APPLYING VARIABLES TO CSS --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: var(--bg-body);
            color: var(--text-main);
            transition: background 0.3s, color 0.3s; /* Smooth transition */
        }
        
        .navbar {
            background: var(--bg-navbar);
            color: var(--text-navbar);
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .navbar h2 { font-size: 20px; }
        
        .navbar-right { display: flex; align-items: center; gap: 20px; }
        
        .user-info { display: flex; align-items: center; gap: 10px; }
        
        .role-badge {
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }
        .role-admin { background: #e74c3c; color: white; }
        .role-user { background: #3498db; color: white; }
        
        .btn-logout, .btn-theme {
            padding: 8px 15px;
            background: #e74c3c;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
            transition: background 0.3s;
            border: none;
            cursor: pointer;
        }
        .btn-logout:hover { background: #c0392b; }
        
        /* Theme Toggle Button Style */
        .theme-toggle {
            text-decoration: none;
            font-size: 20px;
            margin-right: 10px;
            padding: 5px 10px;
            border-radius: 5px;
            background: var(--btn-hover-bg);
            transition: transform 0.2s;
        }
        .theme-toggle:hover { transform: scale(1.1); }

        .container { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
        
        .welcome-card, .stat-card, .quick-actions {
            background: var(--bg-card);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px var(--shadow);
            margin-bottom: 30px;
            transition: background 0.3s;
        }
        
        .welcome-card h1, .stat-content h3, .quick-actions h2 {
            color: var(--text-main);
            margin-bottom: 10px;
        }
        
        .welcome-card p, .stat-content p { color: var(--text-secondary); }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card { display: flex; align-items: center; gap: 20px; padding: 25px; }
        
        .stat-icon {
            font-size: 40px;
            width: 60px; height: 60px;
            display: flex; align-items: center; justify-content: center;
            border-radius: 10px;
        }
        .stat-icon-students { background: #e8f4fd; }
        
        .action-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }
        
        .action-btn {
            padding: 20px;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            text-align: center;
            transition: all 0.3s;
            display: block;
        }
        .action-btn:hover { transform: translateY(-2px); opacity: 0.9; }
        .action-btn-primary { background: #3498db; }
        .action-btn-success { background: #27ae60; }
        .action-btn-warning { background: #f39c12; }
        #session-warning-toast {
            visibility: hidden;
            min-width: 300px;
            background-color: #fff3cd; 
            color: #856404; 
            text-align: center;
            border-radius: 8px;
            padding: 16px;
            position: fixed;
            z-index: 9999;
            right: 30px;
            top: 30px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            border-left: 5px solid #ffc107;
            font-family: 'Segoe UI', sans-serif;
            opacity: 0;
            transition: opacity 0.5s, top 0.5s;
        }

        #session-warning-toast.show {
            visibility: visible;
            opacity: 1;
            top: 50px; 
        }

        .toast-btn {
            background: #856404;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 10px;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h2>📚 Student Management System</h2>
        <div class="navbar-right">
            
            <c:choose>
                <c:when test="${cookie.user_theme.value == 'dark'}">
                    <a href="theme?mode=light" class="theme-toggle" title="Switch to Light Mode">☀️</a>
                </c:when>
                <c:otherwise>
                    <a href="theme?mode=dark" class="theme-toggle" title="Switch to Dark Mode">🌙</a>
                </c:otherwise>
            </c:choose>

            <div class="user-info">
                <span>${sessionScope.fullName}</span>
                <span class="role-badge role-${sessionScope.role}">
                    ${sessionScope.role}
                </span>
            </div>
            
            <a href="change-password" style="color: var(--text-navbar); margin-right: 10px; text-decoration: none; font-size: 14px;">Password</a>
            
            <a href="logout" class="btn-logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <div class="welcome-card">
            <h1>${welcomeMessage}</h1>
            <p>Here's what's happening with your students today.</p>
        </div>
        
    <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon stat-icon-students">👨‍🎓</div>
                <div class="stat-content">
                    <h3>${totalStudents}</h3>
                    <p>Total Students</p>
                </div>
            </div>

            <%
                int myVisitCount = 1;
                long myLastLogin = System.currentTimeMillis();
                
                Cookie[] myCookies = request.getCookies();
                if (myCookies != null) {
                    for (Cookie c : myCookies) {
                        if ("visit_count".equals(c.getName())) {
                            try { myVisitCount = Integer.parseInt(c.getValue()); } catch(Exception e){}
                        } else if ("last_login".equals(c.getName())) {
                            try { myLastLogin = Long.parseLong(c.getValue()); } catch(Exception e){}
                        }
                    }
                }
                
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, HH:mm");
                String lastLoginStr = sdf.format(new java.util.Date(myLastLogin));
            %>
            
            <div class="stat-card">
                <div class="stat-icon" style="background: #fff3e0; color: #f57c00;">📊</div>
                <div class="stat-content">
                    <h3><%= myVisitCount %></h3>
                    <p>My Visits</p>
                    <small style="font-size: 12px; color: var(--text-secondary);">Last: <%= lastLoginStr %></small>
                </div>
            </div>
        </div>
        
        <div class="quick-actions">
            <h2>Quick Actions</h2>
            <div class="action-grid">
                <a href="student?action=list" class="action-btn action-btn-primary">
                    📋 View All Students
                </a>
                
                <c:if test="${sessionScope.role eq 'admin'}">
                    <a href="student?action=new" class="action-btn action-btn-success">
                        ➕ Add New Student
                    </a>
                </c:if>
                
                <a href="student?action=search" class="action-btn action-btn-warning">
                    🔍 Search Students
                </a>
                
                <a href="change-password" class="action-btn" style="background: #6f42c1;">
                    🔒 Change Password
                </a>
            </div>
        </div>
    </div>

    <div id="session-warning-toast">
        ⚠️ <strong>Warning:</strong> Your session will expire in <span id="time-left"></span> minutes.<br>
        <small>Please save your work or move your mouse to extend the session.</small>
    </div>

    <script>
        // --- CONFIGURATION ---
        // 30 minutes (Must match session timeout in LoginController)
        const SESSION_TIMEOUT = 30 * 60 * 1000; 
        
        // Warn user 2 minutes before expiration
        const WARNING_TIME = 2 * 60 * 1000;     

        let lastActivity = Date.now();
        let warningShown = false;

        // Function to update last activity time
        function updateActivity() {
            lastActivity = Date.now();
            
            // If warning is currently shown, hide it because user is active again
            if (warningShown) {
                const toast = document.getElementById("session-warning-toast");
                toast.classList.remove("show");
                warningShown = false;
                console.log("Activity detected. Session extended.");
            }
        }

        // Listen for user interactions to reset the timer
        document.addEventListener('mousemove', updateActivity);
        document.addEventListener('keypress', updateActivity);
        document.addEventListener('click', updateActivity);
        document.addEventListener('scroll', updateActivity);

        // Check session status every second (1000ms)
        setInterval(function() {
            const now = Date.now();
            const timeElapsed = now - lastActivity;
            const timeRemaining = SESSION_TIMEOUT - timeElapsed;

            // 1. If time is up -> Logout user
            if (timeRemaining <= 0) {
                alert('⏰ Your session has expired due to inactivity. Please login again.');
                window.location.href = 'logout';
            } 
            // 2. If entering warning period -> Show toast notification
            else if (timeRemaining <= WARNING_TIME) {
                if (!warningShown) {
                    const toast = document.getElementById("session-warning-toast");
                    toast.classList.add("show");
                    warningShown = true;
                }
                
                // Update the countdown timer in the toast
                const minutesLeft = Math.ceil(timeRemaining / 60000);
                document.getElementById("time-left").innerText = minutesLeft;
            }
        }, 1000); 

        console.log('Session timeout tracking started.');
    </script>
</body>
</html>