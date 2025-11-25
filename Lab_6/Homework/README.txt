STUDENT INFORMATION:
Name: Nguyễn Vũ Thành Tính
Student ID: ITCSIU23039
Class: Monday Morning 1-4

COMPLETED EXERCISES:
[x] Exercise 1: Database & User Model
[x] Exercise 2: User Model & DAO
[x] Exercise 3: Login/Logout Controllers
[x] Exercise 4: Views & Dashboard
[x] Exercise 5: Authentication Filter
[x] Exercise 6: Admin Authorization Filter
[x] Exercise 7: Role-Based UI
[x] Exercise 8: Change Password

AUTHENTICATION COMPONENTS:
- Models: User.java, Student.java
- DAOs: UserDAO.java, StudentDAO.java
- Controllers: 
    - LoginController.java
    - LogoutController.java
    - DashboardController.java
    - StudentController.java
    - ChangePasswordController.java
    - ThemeController.java (Bonus)
- Filters: AuthFilter.java, AdminFilter.java
- Utils: CookieUtil.java (Bonus)
- Views: 
    - login.jsp
    - dashboard.jsp
    - student-list.jsp
    - student-form.jsp
    - change-password.jsp

TEST CREDENTIALS:
Admin:
- Username: admin
- Password: password123

Regular User:
- Username: john
- Password: password123

FEATURES IMPLEMENTED:
- Full User authentication flow (Login/Logout)
- Password hashing with BCrypt
- Secure Session management (Invalidate old session on login)
- Dashboard with real-time statistics
- Authentication filter protecting all secure pages
- Admin authorization filter protecting CRUD operations
- Dynamic Role-based UI (Hide/Show buttons based on role)
- Change Password functionality with validation

SECURITY MEASURES:
- BCrypt password hashing (No plain text passwords)
- Session fixation protection (Session regeneration)
- Session timeout configured (30 minutes)
- SQL injection prevention using PreparedStatement
- XSS prevention using JSTL c:out
- HttpOnly Cookies for sensitive data

KNOWN ISSUES:
1. Hardcoded Database Credentials: 
   The DB username and password are currently hardcoded in the DAO files. In a real-world app, I know I should move these to a properties file or environment variables.

2. Browser Back Button: 
   After logging out, if you press the browser's "Back" button, you might see the cached page of the dashboard. However, if you click any link, the AuthFilter correctly kicks you back to the login page.

3. Mobile Responsiveness:
   Since I wrote custom CSS, the table view might look a bit squashed on very small mobile screens (iPhone SE size). It works perfectly on Desktop and Tablets.

4. Search Function:
   The search is currently exact match or partial match for text fields. It does not support advanced filters like "Date Range".

BONUS FEATURES COMPLETED:
1. [x] Cookie Utility Class: Created reusable CookieUtil.java for cleaner code.
2. [x] Remember Me: Implemented persistent login (30 days) using Database Tokens and Cookies.
3. [x] User Theme Preference: Light/Dark mode switcher persisting via Cookies.
4. [x] Session Timeout Warning: JavaScript warning displayed 2 minutes before session expiry.
5. [x] Activity Logging: Track "Visit Count" and "Last Login Time" using Cookies and display on Dashboard.

TIME SPENT: Approximately 4-5 hours

TESTING NOTES:
- Tested Admin login: Can view, add, edit, delete students.
- Tested User login: Can only view list and search, CRUD buttons are hidden.
- Tested URL Hacking: Direct access to /student?action=delete redirects to error page for non-admins.
- Tested Session Timeout: Warning popup appears after inactivity.
- Tested Theme: Switching to Dark Mode persists after page reload.
- Tested Remember Me: Closing browser and reopening keeps the user logged in.