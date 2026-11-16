<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - JavaWebMVC</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #f0f2f5;
            font-family: Arial, sans-serif;
        }

        .container {
            background: white;
            width: 400px;
            max-width: 92%;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 5px 25px rgba(0,0,0,0.08);
        }

        h2 { text-align: center; margin-bottom: 25px; }

        label {
            display: block;
            margin: 12px 0 6px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .remember {
            display: flex;
            align-items: center;
            gap: 8px;
            margin: 16px 0;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover { background: #1d4ed8; }

        .alert {
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 15px;
        }

        .error { background: #fee2e2; color: #991b1b; }
        .success { background: #dcfce7; color: #166534; }

        .forgot-button {
            display: block;
            margin-top: 12px;
            padding: 11px 12px;
            border: 1px solid #2563eb;
            border-radius: 6px;
            color: #2563eb;
            text-align: center;
            font-weight: 600;
            text-decoration: none;
        }
        .forgot-button:hover { background: #eff6ff; }
        .footer { text-align: center; margin-top: 20px; }
        a { color: #2563eb; text-decoration: none; }
    </style>
</head>
<body>

<div class="container">
    <h2>Login</h2>

    <c:if test="${not empty alert}">
        <div class="alert error">
            <c:out value="${alert}" />
        </div>
    </c:if>

    <c:if test="${param.registered eq '1'}">
        <div class="alert success">
            Registration successful! Please log in.
        </div>
    </c:if>

    <c:if test="${param.activated eq '1'}"><div class="alert success">Account activated successfully.</div></c:if>
    <c:if test="${param.reset eq '1'}"><div class="alert success">Password reset successfully.</div></c:if>
    <form action="${pageContext.request.contextPath}/login"
          method="post">

        <label for="username">Username</label>
        <input type="text"
               id="username"
               name="username"
               value="<c:out value='${rememberedUsername}'/>"
               placeholder="Enter your username"
               autocomplete="username"
               maxlength="100"
               required>

        <label for="password">Password</label>
        <input type="password"
               id="password"
               name="password"
               placeholder="Enter your password"
               autocomplete="current-password"
               required>

        <div class="remember">
            <input type="checkbox" id="remember" name="remember">
            <label for="remember" style="margin:0; font-weight:normal;">
                Remember username
            </label>
        </div>

        <button type="submit">Login</button>
    </form>

    <a class="forgot-button" href="${pageContext.request.contextPath}/forgot-password">Forgot Password?</a>

    <div class="footer">
        <a href="${pageContext.request.contextPath}/activate">Activate account</a><br>
        Don't have an account?
        <a href="${pageContext.request.contextPath}/register">
            Register
        </a>
    </div>
</div>

</body>
</html>
