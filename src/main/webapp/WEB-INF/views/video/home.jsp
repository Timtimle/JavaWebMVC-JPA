<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Latest Videos</title></head>
<body>
<section>
    <div class="page-heading"><h1 class="section-title">Latest videos</h1><p class="text-secondary">10 newest uploads from all users.</p></div>
    <div class="row g-3">
        <c:forEach items="${videos}" var="v">
            <div class="col-sm-6 col-lg-4"><article class="page-card p-3 video-card">
                <a href="${pageContext.request.contextPath}/video/watch?id=${v.id}" class="text-decoration-none">
                    <div class="thumbnail mb-3">&#9654;</div><h2 class="h5"><c:out value="${v.title}"/></h2>
                </a>
                <p class="text-secondary small mb-0">By <c:out value="${v.owner.username}"/> · <c:out value="${v.views}"/> views</p>
            </article></div>
        </c:forEach>
    </div>
    <c:if test="${empty videos}"><div class="alert alert-light border">No videos uploaded yet.</div></c:if>
</section>
</body></html>
