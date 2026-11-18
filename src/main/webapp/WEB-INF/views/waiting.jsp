<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Dashboard - JavaWebMVC</title>

    <style>
        * {
            box-sizing: border-box;
        }

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
            width: 450px;
            max-width: 92%;
            padding: 35px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 5px 25px rgba(0,0,0,0.08);
            text-align: center;
        }

        h1 {
            color: #2563eb;
        }

        .info {
            margin: 25px 0;
            padding: 15px;
            background: #f8fafc;
            border-radius: 8px;
            text-align: left;
        }

        .info p {
            margin: 12px 0;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #dc2626;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover {
            background: #b91c1c;
        }
    </style>
</head>

<body>

<div class="container">

    <h1>Welcome!</h1>

    <p>
        Hello,
        <strong>
            <c:out value="${user.fullName}" />
        </strong>
    </p>

    <p>You have successfully logged in.</p>

    <div class="info">
        <p>
            <strong>Username:</strong>
            <c:out value="${user.username}" />
        </p>

        <p>
            <strong>Email:</strong>
            <c:out value="${user.email}" />
        </p>

        <p>
            <strong>Role:</strong>
            <c:choose>
                <c:when test="${user.roleId == 1}">
                    Administrator
                </c:when>
                <c:otherwise>
                    User
                </c:otherwise>
            </c:choose>
        </p>
    </div>

    
    <a href="${pageContext.request.contextPath}/home" style="display:block;margin:12px 0;">Latest Videos</a>
    <a href="${pageContext.request.contextPath}/video" style="
        display: block;
        padding: 12px;
        margin-bottom: 12px;
        background: #2563eb;
        color: white;
        border-radius: 6px;
        text-decoration: none;
    ">Manage Videos</a>

    <!-- Navigate to category management -->
    <a href="${pageContext.request.contextPath}/category/list"
    style="
        display: block;
        padding: 12px;
        margin-bottom: 12px;
        background: #2563eb;
        color: white;
        border-radius: 6px;
        text-decoration: none;
    ">
        Manage Categories
    </a>

    <form action="${pageContext.request.contextPath}/logout"
          method="post">
        <button type="submit">Logout</button>
    </form>

</div>

</body>
</html>
