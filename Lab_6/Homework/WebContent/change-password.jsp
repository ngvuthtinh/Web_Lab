<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
    <style>
        /* Reuse styles from other pages for consistency */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 500px;
        }
        h1 { color: #333; margin-bottom: 30px; text-align: center; font-size: 24px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; color: #555; font-weight: 500; font-size: 14px; }
        input[type="password"] {
            width: 100%; padding: 12px 15px; border: 2px solid #ddd;
            border-radius: 5px; font-size: 14px; transition: border-color 0.3s;
        }
        input:focus { outline: none; border-color: #667eea; }
        .btn {
            width: 100%; padding: 14px; border: none; border-radius: 5px;
            font-size: 16px; font-weight: 600; cursor: pointer;
            transition: all 0.3s; text-decoration: none; display: inline-block; text-align: center;
        }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4); }
        .btn-secondary { background-color: #6c757d; color: white; margin-top: 10px; }
        .btn-secondary:hover { background-color: #5a6268; }
        
        .message { padding: 15px; margin-bottom: 20px; border-radius: 5px; text-align: center; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error-msg { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .field-error { color: #dc3545; font-size: 13px; margin-top: 5px; display: block; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔒 Change Password</h1>
        
        <c:if test="${not empty message}">
            <div class="message success">✅ ${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error-msg">❌ ${error}</div>
        </c:if>
        
        <form action="change-password" method="POST">
            <div class="form-group">
                <label for="currentPassword">Current Password</label>
                <input type="password" id="currentPassword" name="currentPassword" required>
                <c:if test="${not empty errorCurrent}">
                    <span class="field-error">⚠️ ${errorCurrent}</span>
                </c:if>
            </div>
            
            <div class="form-group">
                <label for="newPassword">New Password (min 8 chars)</label>
                <input type="password" id="newPassword" name="newPassword" required>
                <c:if test="${not empty errorNew}">
                    <span class="field-error">⚠️ ${errorNew}</span>
                </c:if>
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">Confirm New Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                <c:if test="${not empty errorConfirm}">
                    <span class="field-error">⚠️ ${errorConfirm}</span>
                </c:if>
            </div>
            
            <button type="submit" class="btn btn-primary">Update Password</button>
            <a href="dashboard" class="btn btn-secondary">Cancel / Back to Dashboard</a>
        </form>
    </div>
</body>
</html>