<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Video Editor</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body>
<nav><a href="${pageContext.request.contextPath}/home">JavaWebMVC</a><a href="${pageContext.request.contextPath}/video">My Videos</a></nav><main class="form-card">
<h1>${empty video ? 'Upload Video' : 'Edit Video'}</h1>
<c:if test="${not empty alert}"><p class="notice error"><c:out value="${alert}"/></p></c:if>
<form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}${empty video ? '/video/new' : '/video/edit'}${empty video ? '' : '?id='}${empty video ? '' : video.id}">
<label>Title</label><input name="title" maxlength="255" required value="<c:out value='${empty param.title ? video.title : param.title}'/>">
<label>Description</label><textarea name="description" maxlength="2000"><c:out value="${empty param.description ? video.description : param.description}"/></textarea>
<label>Video file (MP4, WebM, OGG; max 100 MB)</label><input type="file" name="videoFile" accept="video/mp4,video/webm,video/ogg" ${empty video ? 'required' : ''}>
<c:if test="${not empty video}"><p class="muted">Leave file blank to keep the current video.</p></c:if>
<button type="submit">Save video</button></form></main></body></html>
