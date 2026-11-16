<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Home - JavaWebMVC</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body>
<nav><a href="${pageContext.request.contextPath}/home">JavaWebMVC</a><a href="${pageContext.request.contextPath}/video">My Videos</a><a href="${pageContext.request.contextPath}/waiting">Account</a><a href="${pageContext.request.contextPath}/login">Login</a></nav>
<main><h1>Latest Videos</h1><p class="muted">10 most recently uploaded videos.</p><div class="grid">
<c:forEach items="${videos}" var="v"><article class="card"><a href="${pageContext.request.contextPath}/video/watch?id=${v.id}"><div class="preview">&#9654;</div><h2><c:out value="${v.title}"/></h2></a>
<p class="muted">By <c:out value="${v.owner.username}"/> &middot; <c:out value="${v.views}"/> views</p></article></c:forEach></div>
<c:if test="${empty videos}"><p>No videos have been uploaded yet.</p></c:if></main></body></html>
