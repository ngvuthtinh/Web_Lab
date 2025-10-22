<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // Get the color parameter from the request
    String color = request.getParameter("color");
    
    // Default background color
    String bgColor = "#FFFFFF"; // white
    
    // Set background color based on button clicked
    if (color != null) {
        switch (color.toLowerCase()) {
            case "red":
                bgColor = "#FF0000";
                break;
            case "blue":
                bgColor = "#0000FF";
                break;
            case "green":
                bgColor = "#00FF00";
                break;
            case "pink":
                bgColor = "#FFC0CB";
                break;
            case "yellow":
                bgColor = "#FFFF00";
                break;
            default:
                bgColor = "#FFFFFF";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Background Color Changer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: <%= bgColor %>;
            transition: background-color 0.3s ease;
        }
        
        .container {
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
            background-color: rgba(255, 255, 255, 0.9);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        h1 {
            color: #333;
            margin-bottom: 30px;
        }
        
        .button-group {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            justify-content: center;
            margin-top: 20px;
        }
        
        .color-button {
            padding: 15px 30px;
            font-size: 16px;
            font-weight: bold;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            color: white;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
            min-width: 120px;
        }
        
        .color-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        
        .color-button:active {
            transform: translateY(0);
        }
        
        .btn-red {
            background-color: #FF0000;
        }
        
        .btn-blue {
            background-color: #0000FF;
        }
        
        .btn-green {
            background-color: #00FF00;
            color: #333;
            text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.5);
        }
        
        .btn-pink {
            background-color: #FFC0CB;
            color: #333;
            text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.5);
        }
        
        .btn-yellow {
            background-color: #FFFF00;
            color: #333;
            text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.5);
        }
        
        .current-color {
            margin-top: 30px;
            padding: 15px;
            background-color: rgba(0, 0, 0, 0.05);
            border-radius: 5px;
        }
        
        .current-color strong {
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Background Color Changer</h1>
        <p>Click a button to change the background color</p>
        
        <form method="GET" action="BG_color.jsp">
            <div class="button-group">
                <button type="submit" name="color" value="red" class="color-button btn-red">
                    Red
                </button>
                <button type="submit" name="color" value="blue" class="color-button btn-blue">
                    Blue
                </button>
                <button type="submit" name="color" value="green" class="color-button btn-green">
                    Green
                </button>
                <button type="submit" name="color" value="pink" class="color-button btn-pink">
                    Pink
                </button>
                <button type="submit" name="color" value="yellow" class="color-button btn-yellow">
                    Yellow
                </button>
            </div>
        </form>
        
        <div class="current-color">
            <% if (color != null) { %>
                <strong>Current Background Color:</strong> 
                <span style="text-transform: capitalize;"><%= color %></span> 
                (<%= bgColor %>)
            <% } else { %>
                <strong>Current Background Color:</strong> Default (White)
            <% } %>
        </div>
    </div>
</body>
</html>
