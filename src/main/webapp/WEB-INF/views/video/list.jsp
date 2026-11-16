<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>My Videos</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body>
<nav><a href="${pageContext.request.contextPath}/home">JavaWebMVC</a><a href="${pageContext.request.contextPath}/video">My Videos</a><a href="${pageContext.request.contextPath}/waiting">Account</a></nav>
<main><div class="actions"><h1>My Videos</h1><a class="button" href="${pageContext.request.contextPath}/video/new">Upload video</a></div>
<p class="muted">Six videos per page.</p><div class="grid">
<c:forEach items="${videos}" var="v"><article class="card"><a href="${pageContext.request.contextPath}/video/watch?id=${v.id}"><div class="preview">&#9654;</div><h2><c:out value="${v.title}"/></h2></a><p class="muted"><c:out value="${v.views}"/> views</p>
<div class="actions"><a href="${pageContext.request.contextPath}/video/edit?id=${v.id}">Edit</a><form method="post" action="${pageContext.request.contextPath}/video/delete?id=${v.id}" onsubmit="return confirm('Delete this video?')"><button class="danger" type="submit">Delete</button></form></div></article></c:forEach></div>
<c:if test="${empty videos}"><p>No videos yet. Upload your first video.</p></c:if>
<div class="pagination"><c:if test="${page gt 1}"><a href="${pageContext.request.contextPath}/video?page=${page - 1}">Previous</a></c:if><span>Page ${page} / ${pages}</span><c:if test="${page lt pages}"><a href="${pageContext.request.contextPath}/video?page=${page + 1}">Next</a></c:if></div>
</main></body></html>
