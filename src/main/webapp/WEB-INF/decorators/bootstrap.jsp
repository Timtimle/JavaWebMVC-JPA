<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="vn.edu.hcmute.model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String ctx = request.getContextPath();
    User current = session.getAttribute("account") instanceof User ? (User) session.getAttribute("account") : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/> | JavaWebMVC</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= ctx %>/assets/css/site.css">
    <sitemesh:write property="head"/>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-semibold" href="<%= ctx %>/home">JavaWebMVC</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#siteNav"
                    aria-controls="siteNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="siteNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/home">Home</a></li>
                    <% if (current != null) { %>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/video">My Videos</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/category/list">Categories</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/profile">Profile</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= ctx %>/waiting">Dashboard</a></li>
                    <% } %>
                </ul>
                <div class="d-flex gap-2 align-items-center">
                    <% if (current == null) { %>
                        <a class="btn btn-outline-light btn-sm" href="<%= ctx %>/login">Login</a>
                        <a class="btn btn-primary btn-sm" href="<%= ctx %>/register">Register</a>
                    <% } else { %>
                        <span class="navbar-text me-2"><c:out value="${sessionScope.account.username}"/></span>
                        <form action="<%= ctx %>/logout" method="post" class="m-0">
                            <button class="btn btn-outline-light btn-sm" type="submit">Logout</button>
                        </form>
                    <% } %>
                </div>
            </div>
        </div>
    </nav>
    <div class="container py-4">
        <sitemesh:write property="body"/>
    </div>
    <footer class="text-center text-secondary small py-4">JavaWebMVC · Servlet · JPA</footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<%= ctx %>/assets/js/validation.js"></script>
</body>
</html>
