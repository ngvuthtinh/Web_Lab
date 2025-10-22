<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // Get parameters from the form
    String num1Str = request.getParameter("num1");
    String num2Str = request.getParameter("num2");
    String operation = request.getParameter("operation");
    
    // Variables for calculation
    Double num1 = null;
    Double num2 = null;
    Double result = null;
    String errorMessage = null;
    boolean hasCalculation = false;
    
    // Process calculation if form is submitted
    if (num1Str != null && num2Str != null && operation != null) {
        hasCalculation = true;
        try {
            num1 = Double.parseDouble(num1Str);
            num2 = Double.parseDouble(num2Str);
            
            switch (operation) {
                case "add":
                    result = num1 + num2;
                    break;
                case "subtract":
                    result = num1 - num2;
                    break;
                case "multiply":
                    result = num1 * num2;
                    break;
                case "divide":
                    if (num2 == 0) {
                        errorMessage = "Error: Cannot divide by zero!";
                    } else {
                        result = num1 / num2;
                    }
                    break;
                default:
                    errorMessage = "Invalid operation";
            }
        } catch (NumberFormatException e) {
            errorMessage = "Error: Please enter valid numbers!";
        }
    }
    
    // Get operation symbol for display
    String getOperationSymbol(String op) {
        if (op == null) return "";
        switch (op) {
            case "add": return "+";
            case "subtract": return "-";
            case "multiply": return "×";
            case "divide": return "÷";
            default: return "";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JSP Calculator</title>
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
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .calculator-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 500px;
            width: 100%;
        }
        
        h1 {
            text-align: center;
            color: #667eea;
            margin-bottom: 30px;
            font-size: 32px;
        }
        
        .result-display {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            min-height: 80px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        
        .result-display .expression {
            font-size: 18px;
            margin-bottom: 10px;
            opacity: 0.9;
        }
        
        .result-display .result {
            font-size: 36px;
            font-weight: bold;
        }
        
        .error-message {
            background: #ff4757;
            color: white;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            text-align: center;
        }
        
        .input-group {
            margin-bottom: 20px;
        }
        
        .input-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 600;
        }
        
        .input-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 18px;
            transition: border-color 0.3s;
        }
        
        .input-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .operation-buttons {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 10px;
            margin-bottom: 20px;
        }
        
        .operation-btn {
            padding: 15px;
            font-size: 24px;
            font-weight: bold;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s;
            color: white;
        }
        
        .operation-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        }
        
        .operation-btn:active {
            transform: translateY(0);
        }
        
        .btn-add {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .btn-subtract {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .btn-multiply {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        .btn-divide {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        .clear-btn {
            width: 100%;
            padding: 15px;
            background: #ff4757;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .clear-btn:hover {
            background: #ff3838;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 71, 87, 0.4);
        }
        
        .info-text {
            text-align: center;
            color: #666;
            margin-top: 20px;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="calculator-container">
        <h1>🧮 JSP Calculator</h1>
        
        <% if (errorMessage != null) { %>
            <div class="error-message">
                ⚠️ <%= errorMessage %>
            </div>
        <% } %>
        
        <% if (hasCalculation && errorMessage == null && result != null) { %>
            <div class="result-display">
                <div class="expression">
                    <%= num1 %> <%= getOperationSymbol(operation) %> <%= num2 %>
                </div>
                <div class="result">
                    = <%= String.format("%.4f", result) %>
                </div>
            </div>
        <% } else if (!hasCalculation) { %>
            <div class="result-display">
                <div class="result">Ready</div>
            </div>
        <% } %>
        
        <form method="POST" action="Calculator.jsp">
            <div class="input-group">
                <label for="num1">First Number:</label>
                <input 
                    type="number" 
                    id="num1" 
                    name="num1" 
                    step="any" 
                    required 
                    value="<%= num1 != null ? num1 : "" %>"
                    placeholder="Enter first number"
                >
            </div>
            
            <div class="input-group">
                <label for="num2">Second Number:</label>
                <input 
                    type="number" 
                    id="num2" 
                    name="num2" 
                    step="any" 
                    required 
                    value="<%= num2 != null ? num2 : "" %>"
                    placeholder="Enter second number"
                >
            </div>
            
            <div class="operation-buttons">
                <button type="submit" name="operation" value="add" class="operation-btn btn-add">
                    +
                </button>
                <button type="submit" name="operation" value="subtract" class="operation-btn btn-subtract">
                    −
                </button>
                <button type="submit" name="operation" value="multiply" class="operation-btn btn-multiply">
                    ×
                </button>
                <button type="submit" name="operation" value="divide" class="operation-btn btn-divide">
                    ÷
                </button>
            </div>
        </form>
        
        <button type="button" class="clear-btn" onclick="window.location.href='Calculator.jsp'">
            Clear
        </button>
        
        <p class="info-text">
            Enter two numbers and click an operation button
        </p>
    </div>
</body>
</html>
