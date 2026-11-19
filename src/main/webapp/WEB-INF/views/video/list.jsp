<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>My Videos</title></head>
<body>
<section>
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 page-heading">
        <div><h1 class="section-title mb-1">My videos</h1><p class="text-secondary mb-0">6 videos per page.</p></div>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/video/new">Upload video</a>
    </div>
    <div class="row g-3"><c:forEach var="v" items="${videos}"><div class="col-sm-6 col-lg-4">
        <article class="page-card p-3 video-card">
            <a class="text-decoration-none" href="${pageContext.request.contextPath}/video/watch?id=${v.id}">
                <div class="thumbnail mb-3">&#9654;</div><h2 class="h5"><c:out value="${v.title}"/></h2>
            </a>
            <p class="text-secondary small"><c:out value="${v.views}"/> views</p>
            <div class="d-flex gap-2"><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/video/edit?id=${v.id}">Edit</a>
                <form method="post" action="${pageContext.request.contextPath}/video/delete?id=${v.id}" onsubmit="return confirm('Delete video?')">
                    <button class="btn btn-sm btn-outline-danger" type="submit">Delete</button>
                </form>
            </div>
        </article>
    </div></c:forEach></div>
    <c:if test="${empty videos}"><div class="alert alert-light border">No videos yet. Upload your first video.</div></c:if>
    <div class="d-flex justify-content-center align-items-center gap-3 mt-4">
        <c:if test="${page gt 1}"><a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/video?page=${page - 1}">Previous</a></c:if>
        <span>Page ${page} / ${pages}</span>
        <c:if test="${page lt pages}"><a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/video?page=${page + 1}">Next</a></c:if>
    </div>
</section>
</body></html>
