<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Add Category</title></head>
<body>
<section class="form-card page-card">
        <h1 class="h3 mb-3">Add category</h1>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/category/add" enctype="multipart/form-data" data-validate novalidate>
            <div class="mb-3"><label class="form-label" for="catename">Category name</label>
                <input class="form-control" id="catename" type="text" name="catename" maxlength="255" required>
                <div class="invalid-feedback">Enter a category name (max 255).</div>
            </div>
            <div class="mb-4"><label class="form-label" for="icon">Image (optional)</label>
                <input class="form-control" id="icon" type="file" name="icon" accept="image/jpeg,image/png,image/gif,.jpg,.png,.gif">
                <div class="form-text">JPG, PNG or GIF · max 5 MB.</div>
            </div>
            <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Add category</button>
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/category/list">Cancel</a></div>
        </form>
    </section>
</body></html>
