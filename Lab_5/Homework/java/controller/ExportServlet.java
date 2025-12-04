package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {
    
    private StudentDAO studentDAO;
    
    public void init() { studentDAO = new StudentDAO(); }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all students (You could also reuse the combined method to export only filtered results!)
        List<Student> students = studentDAO.getAllStudents();
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Students List");
            
            // Create Header
            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Code");
            header.createCell(2).setCellValue("Full Name");
            header.createCell(3).setCellValue("Email");
            header.createCell(4).setCellValue("Major");
            
            // Fill Data
            int rowIdx = 1;
            for (Student s : students) {
                XSSFRow row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getStudentCode());
                row.createCell(2).setCellValue(s.getFullName());
                row.createCell(3).setCellValue(s.getEmail());
                row.createCell(4).setCellValue(s.getMajor());
            }
            
            // Auto size columns
            for(int i=0; i<5; i++) sheet.autoSizeColumn(i);
            
            // Download Setup
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");
            
            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
        }
    }
}