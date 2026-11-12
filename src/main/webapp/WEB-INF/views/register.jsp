<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - JavaWebMVC</title>

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
            width: 420px;
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

        input {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        button {
            width: 100%;
            padding: 12px;
            margin-top: 20px;
            background: #16a34a;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover { background: #15803d; }

        .alert {
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 15px;
            background: #fee2e2;
            color: #991b1b;
        }

        .footer { text-align: center; margin-top: 20px; }
        a { color: #2563eb; text-decoration: none; }
    </style>
</head>
<body>

<div class="container">
    <h2>Create Account</h2>

    <c:if test="${not empty alert}">
        <div class="alert">
            <c:out value="${alert}" />
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register"
          method="post">

        <label for="username">Username *</label>
        <input type="text"
               id="username"
               name="username"
               value="<c:out value='${username}'/>"
               placeholder="Enter username"
               maxlength="100"
               autocomplete="username"
               required>

        <label for="email">Email *</label>
        <input type="email"
               id="email"
               name="email"
               value="<c:out value='${email}'/>"
               placeholder="Enter email address"
               maxlength="255"
               autocomplete="email"
               required>

        <label for="fullname">Full Name</label>
        <input type="text"
               id="fullname"
               name="fullname"
               value="<c:out value='${fullname}'/>"
               placeholder="Enter full name"
               maxlength="255">

        <label for="phone">Phone</label>
        <input type="tel"
               id="phone"
               name="phone"
               value="<c:out value='${phone}'/>"
               placeholder="Enter phone number"
               maxlength="20">

        <label for="password">Password *</label>
        <input type="password"
               id="password"
               name="password"
               placeholder="At least 8 characters"
               minlength="8"
               autocomplete="new-password"
               required>

        <button type="submit">Register</button>
    </form>

    <div class="footer">
        Already have an account?
        <a href="${pageContext.request.contextPath}/login">
            Login
        </a>
    </div>
</div>

</body>
</html>
