<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Watch Video</title></head>
<body>
<section class="page-card p-4">
    <h1 class="h3 mb-1"><c:out value="${video.title}"/></h1>
    <p class="text-secondary small">By <c:out value="${video.owner.username}"/> · <c:out value="${video.views + 1}"/> views</p>
    <video class="video-player" controls preload="metadata" playsinline src="${pageContext.request.contextPath}/video/file?id=${video.id}">
        Your browser does not support HTML5 video.
    </video>
    <c:if test="${not empty video.description}"><div class="mt-3"><h2 class="h5">Description</h2>
        <p class="description"><c:out value="${video.description}"/></p></div></c:if>
    <a class="btn btn-outline-secondary mt-2" href="${pageContext.request.contextPath}/home">Back to latest videos</a>
</section>
</body></html>
