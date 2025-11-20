<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-style: italic;
        }
        
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
        }
        
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-danger {
            background-color: #dc3545;
            color: white;
            padding: 8px 16px;
            font-size: 13px;
        }
        
        .btn-danger:hover {
            background-color: #c82333;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 13px;
            letter-spacing: 0.5px;
        }
        
        tbody tr {
            transition: background-color 0.2s;
        }
        
        tbody tr:hover {
            background-color: #f8f9fa;
        }
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
        /* --- Search Form Styles --- */
        .search-container {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 1px solid #e9ecef;
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }
        
        .search-form {
            display: flex;
            gap: 10px;
            flex: 1;
            min-width: 300px;
        }
        
        .search-input {
            flex: 1;
            padding: 10px 15px;
            border: 1px solid #ced4da;
            border-radius: 5px;
            font-size: 14px;
        }
        
        .search-result-msg {
            margin-bottom: 15px;
            color: #495057;
            font-size: 15px;
        }
        /* --- Filter & Sort Styles --- */
        .filter-box {
            background: #e9ecef;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #dee2e6;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .filter-box select {
            width: auto;
            padding: 8px;
            margin: 0 10px;
        }
        
        th a {
            color: white;
            text-decoration: none;
            display: block;
            width: 100%;
        }
        
        th a:hover {
            color: #e2e6ea;
            text-decoration: underline;
        }
        
        .sort-icon {
            font-size: 10px;
            margin-left: 5px;
            vertical-align: middle;
        }
        .pagination-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .pagination {
            display: flex;
            gap: 5px;
        }
        
        .pagination a, .pagination span {
            padding: 8px 12px;
            text-decoration: none;
            border: 1px solid #ddd;
            color: #667eea;
            border-radius: 4px;
            transition: all 0.3s;
        }
        
        .pagination a:hover {
            background-color: #f8f9fa;
            border-color: #667eea;
        }
        
        .pagination .active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-color: transparent;
        }
        
        .page-info {
            color: #666;
            font-size: 14px;
        }
        .btn-success{
            background-color: #28a745;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Student Management System</h1>
        <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>
        <c:if test="${not empty param.message}"><div style="color: green; margin-bottom: 10px;">✅ ${param.message}</div></c:if>
        
        <div class="control-panel">
            <a href="student?action=new" class="btn btn-primary">➕ Add Student</a>
            
            <a href="export" class="btn btn-success">📥 Export Excel</a>
            
            <form action="student" method="GET" style="display: flex; gap: 10px; align-items: center; flex: 1;">
                <input type="hidden" name="action" value="list">
                <input type="hidden" name="sortBy" value="${currentSortBy}">
                <input type="hidden" name="order" value="${currentOrder}">
                
                <input type="text" name="keyword" placeholder="Search name/code..." value="${keyword}" style="flex: 1;">
                
                <select name="major" onchange="this.form.submit()">
                    <option value="">-- All Majors --</option>
                    <option value="Computer Science" ${currentMajor == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" ${currentMajor == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Software Engineering" ${currentMajor == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                    <option value="Business Administration" ${currentMajor == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                </select>
                
                <button type="submit" class="btn btn-primary">🔍</button>
                <a href="student?action=list" class="btn btn-secondary">Clear</a>
            </form>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th><a href="student?action=list&sortBy=id&order=${currentSortBy=='id' && currentOrder=='asc'?'desc':'asc'}&keyword=${keyword}&major=${currentMajor}">ID ${currentSortBy=='id' ? (currentOrder=='asc'?'▲':'▼') : ''}</a></th>
                    <th><a href="student?action=list&sortBy=student_code&order=${currentSortBy=='student_code' && currentOrder=='asc'?'desc':'asc'}&keyword=${keyword}&major=${currentMajor}">Code ${currentSortBy=='student_code' ? (currentOrder=='asc'?'▲':'▼') : ''}</a></th>
                    <th><a href="student?action=list&sortBy=full_name&order=${currentSortBy=='full_name' && currentOrder=='asc'?'desc':'asc'}&keyword=${keyword}&major=${currentMajor}">Full Name ${currentSortBy=='full_name' ? (currentOrder=='asc'?'▲':'▼') : ''}</a></th>
                    <th>Email</th>
                    <th>Major</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${students}">
                    <tr>
                        <td>${s.id}</td>
                        <td>${s.studentCode}</td>
                        <td>${s.fullName}</td>
                        <td>${s.email}</td>
                        <td>${s.major}</td>
                        <td style="white-space: nowrap;"> <a href="student?action=edit&id=${s.id}" 
                               class="btn btn-secondary" 
                               style="padding: 12px 24px; font-size: 15px; display: inline-block; min-width: 80px; text-align: center;">
                               ✏️ Edit
                            </a>

                            <a href="student?action=delete&id=${s.id}" 
                               class="btn btn-danger" 
                               onclick="return confirm('Are you sure you want to delete this student?')"
                               style="padding: 12px 24px; font-size: 15px; display: inline-block; min-width: 80px; text-align: center;">
                               🗑️ Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty students}">
                    <tr><td colspan="6" style="text-align: center; padding: 30px;">No students found</td></tr>
                </c:if>
            </tbody>
        </table>
        
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="student?action=list&page=${currentPage-1}&keyword=${keyword}&major=${currentMajor}&sortBy=${currentSortBy}&order=${currentOrder}">«</a>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <span class="active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="student?action=list&page=${i}&keyword=${keyword}&major=${currentMajor}&sortBy=${currentSortBy}&order=${currentOrder}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="student?action=list&page=${currentPage+1}&keyword=${keyword}&major=${currentMajor}&sortBy=${currentSortBy}&order=${currentOrder}">»</a>
                </c:if>
            </div>
            <div style="text-align: center; margin-top: 10px; color: #666;">
                Page ${currentPage} of ${totalPages} (Total: ${totalRecords})
            </div>
        </c:if>
    </div>
</body>
</html>
