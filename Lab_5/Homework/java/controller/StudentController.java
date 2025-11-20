package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {
    
    private StudentDAO studentDAO;
    
    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "new": showNewForm(request, response); break;
            case "edit": showEditForm(request, response); break;
            case "delete": deleteStudent(request, response); break;
            // Gom tất cả các hành động xem danh sách vào một chỗ
            case "search":
            case "sort":
            case "filter":
            case "list":
            default:
                handleList(request, response); // Hàm xử lý chung (Bonus 3)
                break;
        }
    }

    // --- BONUS 3: UNIFIED LIST HANDLING METHOD ---
    private void handleList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get all Parameters
        String keyword = request.getParameter("keyword");
        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        
        // 2. Pagination Config
        int page = 1;
        int recordsPerPage = 5; // Số lượng bản ghi trên 1 trang
        if (request.getParameter("page") != null) {
            try { page = Integer.parseInt(request.getParameter("page")); } catch(Exception e) {}
        }
        
        // 3. Calculate Offset
        int offset = (page - 1) * recordsPerPage;
        
        // 4. Get Data using Master DAO Method
        List<Student> students = studentDAO.getStudentsCombined(keyword, major, sortBy, order, offset, recordsPerPage);
        int totalRecords = studentDAO.getTotalStudentsCombined(keyword, major);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        
        // 5. Set Attributes (Data + State Preservation)
        request.setAttribute("students", students);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        
        // Important: Send back filters so JSP can keep them in hidden fields/links
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentMajor", major);
        request.setAttribute("currentSortBy", sortBy);
        request.setAttribute("currentOrder", order);
        
        request.getRequestDispatcher("/views/student-list.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
        }
    }
    
    // List all students
    // Modified listStudents to support Pagination (Exercise 8.2)
    private void listStudents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Configuration
        int page = 1;
        int recordsPerPage = 5; // Để 5 cho dễ test, thực tế thường là 10 hoặc 20
        
        // 2. Get requested page number
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 on error
            }
        }
        
        // 3. Calculate Offset
        // Formula: (currentPage - 1) * recordsPerPage
        // Example: Page 1 -> offset 0. Page 2 -> offset 5.
        int offset = (page - 1) * recordsPerPage;
        
        // 4. Fetch Data from DAO
        List<Student> students = studentDAO.getStudentsPaginated(offset, recordsPerPage);
        int totalRecords = studentDAO.getTotalStudents();
        
        // 5. Calculate Total Pages
        // Example: 12 records / 5 per page = 2.4 -> ceil -> 3 pages
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        
        // 6. Set Attributes for View
        request.setAttribute("students", students);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords); // Optional: to show "Total: 50 students"
        
        // Forward
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }
    
    // Show form for new student
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    // Show form for editing student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);
        
        request.setAttribute("student", existingStudent);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    // Insert new student with Validation
    private void insertStudent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");
        
        Student newStudent = new Student(studentCode, fullName, email, major);
        
        // --- VALIDATION STEP ---
        if (!validateStudent(newStudent, request)) {
            // 1. Validation FAILED
            // Set student object back to request to preserve user input in the form
            request.setAttribute("student", newStudent);
            
            // Forward back to form to show error messages
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return; // STOP execution here, do not save to DB
        }
        
        // 2. Validation PASSED -> Proceed to Database
        if (studentDAO.addStudent(newStudent)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }
    
    // Update student with Validation
    private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get ID carefully
        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");
        
        Student student = new Student(studentCode, fullName, email, major);
        student.setId(id); // Important: Set the ID for update
        
        // --- VALIDATION STEP ---
        if (!validateStudent(student, request)) {
            // 1. Validation FAILED
            // Set student object back to request to preserve user input
            request.setAttribute("student", student);
            
            // Forward back to form to show error messages
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return; // STOP execution here
        }
        
        // 2. Validation PASSED -> Proceed to Database
        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }
    
    // Delete student
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }
    // Search students handling
    private void searchStudents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get keyword parameter from the search form
        String keyword = request.getParameter("keyword");
        
        List<Student> students;
        
        // 2. Decide which DAO method to call (Handle null/empty keyword)
        if (keyword != null && !keyword.trim().isEmpty()) {
            // If keyword exists, perform search
            students = studentDAO.searchStudents(keyword);
        } else {
            // If keyword is empty/null, show all students (fallback to list)
            students = studentDAO.getAllStudents();
        }
        
        // 3. Set request attributes
        // "students": The list of results to display in the table
        request.setAttribute("students", students);
        
        // "keyword": To keep the search box filled with what the user typed
        request.setAttribute("keyword", keyword); 
        
        // 4. Forward to the same view as listStudents
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }
    // 6.1: Server-side validation method to prevent invalid data
    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;
        
        // 1. Validate Student Code
        // Rule: Cannot be empty AND must match pattern (2 uppercase letters + 3 or more digits)
        String studentCode = student.getStudentCode();
        if (studentCode == null || studentCode.trim().isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else {
            // Pattern: [A-Z]{2} means 2 uppercase chars, [0-9]{3,} means 3 or more digits
            String codePattern = "[A-Z]{2}[0-9]{3,}";
            if (!studentCode.matches(codePattern)) {
                request.setAttribute("errorCode", "Invalid format. Use 2 uppercase letters + 3+ digits (e.g., SV001)");
                isValid = false;
            }
        }
        
        // 2. Validate Full Name
        // Rule: Cannot be empty AND must have at least 2 characters
        String fullName = student.getFullName();
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        } else if (fullName.trim().length() < 2) {
            request.setAttribute("errorName", "Full name must be at least 2 characters");
            isValid = false;
        }
        
        // 3. Validate Email
        // Rule: Required (due to DB constraint) AND must be valid email format
        String email = student.getEmail();
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorEmail", "Email is required");
            isValid = false;
        } else {
            // Simple regex to check for @ and .
            String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailPattern)) {
                request.setAttribute("errorEmail", "Invalid email format (e.g., name@example.com)");
                isValid = false;
            }
        }
        
        // 4. Validate Major
        // Rule: Cannot be empty
        String major = student.getMajor();
        if (major == null || major.trim().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }
        
        return isValid;
    }
  
    private void filterAndSortStudents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get parameters from the request (URL)
        String major = request.getParameter("major");   // e.g., "Computer Science"
        String sortBy = request.getParameter("sortBy"); // e.g., "full_name"
        String order = request.getParameter("order");   // e.g., "asc" or "desc"
        
        // 2. Call the Combined DAO method (Exercise 7.1)
        // This handles filtering by major AND sorting at the same time!
        List<Student> students = studentDAO.getStudentsFiltered(major, sortBy, order);
        
        // 3. Set attributes to display data and MAINTAIN state in JSP
        request.setAttribute("students", students);
        
        // Send back the parameters so the JSP can keep the dropdowns/links selected
        request.setAttribute("currentMajor", major);
        request.setAttribute("currentSortBy", sortBy);
        request.setAttribute("currentOrder", order);
        
        // 4. Forward to the list view
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }
}
