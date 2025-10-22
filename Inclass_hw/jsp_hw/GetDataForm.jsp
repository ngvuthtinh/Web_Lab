<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // Helper to escape HTML special characters
    String escapeHtml(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<': out.append("&lt;"); break;
                case '>': out.append("&gt;"); break;
                case '&': out.append("&amp;"); break;
                case '"': out.append("&quot;"); break;
                case '\'': out.append("&#x27;"); break;
                case '/': out.append("&#x2F;"); break;
                default: out.append(c);
            }
        }
        return out.toString();
    }

    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");

    boolean passwordsMatch = false;
    if (password != null && confirmPassword != null) {
        passwordsMatch = password.equals(confirmPassword);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Data</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; }
        td { padding: 6px 12px; vertical-align: top; }
        .ok { color: green; }
        .error { color: red; }
    </style>
</head>
<body>
    <h1>Submitted Registration Data</h1>

    <table>
        <tr><td><strong>Full Name</strong></td>
            <td><%= escapeHtml(name) %></td>
        </tr>
        <tr><td><strong>Email</strong></td>
            <td><%= escapeHtml(email) %></td>
        </tr>
        <tr><td><strong>Username</strong></td>
            <td><%= escapeHtml(username) %></td>
        </tr>
        <tr><td><strong>Password</strong></td>
            <td><em>Password received (hidden)</em></td>
        </tr>
        <tr><td><strong>Confirm Password</strong></td>
            <td><em>Confirm received (hidden)</em></td>
        </tr>
    </table>

    <p>
        <% if (password == null && confirmPassword == null) { %>
            <span class="error">No password fields submitted.</span>
        <% } else if (passwordsMatch) { %>
            <span class="ok">Passwords match.</span>
        <% } else { %>
            <span class="error">Passwords do not match.</span>
        <% } %>
    </p>

    <p>
        <a href="GetDataForm.html">Back to form</a>
    </p>

</body>
</html>
