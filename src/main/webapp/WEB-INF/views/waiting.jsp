<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Dashboard</title></head>
<body>
<section class="page-card p-4 mx-auto" style="max-width:750px">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
        <div><p class="text-secondary small mb-1">Dashboard</p><h1 class="h3 mb-0">Welcome, <c:out value="${user.fullName}"/></h1></div>
        <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/profile">Edit profile</a>
    </div>
    <div class="bg-light rounded-3 p-3 mb-4">
        <p class="mb-2"><strong>Username:</strong> <c:out value="${user.username}"/></p>
        <p class="mb-2"><strong>Email:</strong> <c:out value="${user.email}"/></p>
        <p class="mb-0"><strong>Role:</strong> <c:choose><c:when test="${user.roleId == 1}">Administrator</c:when><c:otherwise>User</c:otherwise></c:choose></p>
    </div>
    <div class="d-flex gap-2 flex-wrap">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Latest videos</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/video">Manage videos</a>
        <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/category/list">Categories</a>
    </div>
</section>
</body></html>
