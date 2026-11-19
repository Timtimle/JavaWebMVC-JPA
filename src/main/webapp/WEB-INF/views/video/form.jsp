<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>${empty video ? 'Upload Video' : 'Edit Video'}</title></head>
<body>
<section class="form-card page-card">
        <h1 class="h3 mb-2">${empty video ? 'Upload video' : 'Edit video'}</h1>
        <p class="text-secondary mb-3">${empty video ? 'Add a video to your collection.' : 'Edit details or replace the file.'}</p>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <form method="post" enctype="multipart/form-data" data-validate novalidate
              action="${pageContext.request.contextPath}${empty video ? '/video/new' : '/video/edit'}${empty video ? '' : '?id='}${empty video ? '' : video.id}">
            <div class="mb-3"><label class="form-label" for="title">Title</label>
                <input class="form-control" id="title" type="text" name="title" maxlength="255" required value="<c:out value='${formTitle}'/>">
                <div class="invalid-feedback">Enter a video title (max 255).</div>
            </div>
            <div class="mb-3"><label class="form-label" for="description">Description</label>
                <textarea class="form-control" id="description" name="description" rows="4" maxlength="2000"><c:out value="${formDescription}"/></textarea>
            </div>
            <div class="mb-3"><label class="form-label" for="videoFile">Video file</label>
                <input class="form-control" id="videoFile" type="file" name="videoFile" accept=".mp4,.webm,.ogg,video/mp4,video/webm,video/ogg" ${empty video ? 'required' : ''}>
                <div class="form-text">MP4, WebM or OGG · max 100 MB.
                    <c:if test="${not empty video}">Leave empty to keep the current video.</c:if></div>
                <div class="invalid-feedback">Select a video file.</div>
            </div>
            <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">${empty video ? 'Upload video' : 'Save changes'}</button>
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/video">Cancel</a></div>
        </form>
    </section>
</body></html>
