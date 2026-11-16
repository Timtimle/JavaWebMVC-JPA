<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Watch Video</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body>
<nav><a href="${pageContext.request.contextPath}/home">JavaWebMVC</a><a href="${pageContext.request.contextPath}/video">My Videos</a></nav>
<main><h1><c:out value="${video.title}"/></h1><p class="muted">Uploaded by <c:out value="${video.owner.username}"/> &middot; <c:out value="${video.views + 1}"/> views</p>
<video class="video-player" controls preload="metadata" src="${pageContext.request.contextPath}/video/file?id=${video.id}">Your browser does not support HTML5 video.</video>
<p><c:out value="${video.description}"/></p></main></body></html>
