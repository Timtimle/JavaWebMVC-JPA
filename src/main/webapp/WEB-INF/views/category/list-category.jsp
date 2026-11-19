<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Categories</title></head>
<body>
<section class="page-card p-4">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
        <h1 class="h3 mb-0">Categories</h1>
        <a class="btn btn-success" href="${pageContext.request.contextPath}/category/add">Add category</a>
    </div>
    <form method="get" action="${pageContext.request.contextPath}/category/list" class="row g-2 mb-3">
        <div class="col-md-8"><input class="form-control" name="keyword" maxlength="255" value="<c:out value='${keyword}'/>" placeholder="Search categories..."></div>
        <div class="col-auto"><button class="btn btn-primary" type="submit">Search</button></div>
        <div class="col-auto"><a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/category/list">Reset</a></div>
    </form>
    <div class="table-responsive"><table class="table table-hover align-middle">
        <thead class="table-light"><tr><th>ID</th><th>Image</th><th>Category</th><th>Actions</th></tr></thead>
        <tbody><c:forEach var="cate" items="${cateList}"><tr>
            <td><c:out value="${cate.id}"/></td>
            <td><c:choose><c:when test="${not empty cate.icon}">
                <c:url var="categoryImageUrl" value="/category/image"><c:param name="name" value="${cate.icon}"/></c:url>
                <img class="category-image" src="${categoryImageUrl}" alt="Category image"></c:when>
                <c:otherwise><span class="text-secondary">No image</span></c:otherwise></c:choose></td>
            <td><c:out value="${cate.name}"/></td>
            <td><div class="d-flex gap-2"><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/category/edit?id=${cate.id}">Edit</a>
                <form method="post" action="${pageContext.request.contextPath}/category/delete" onsubmit="return confirm('Delete category?')">
                    <input type="hidden" name="id" value="${cate.id}"><button class="btn btn-sm btn-outline-danger" type="submit">Delete</button>
                </form></div></td>
        </tr></c:forEach></tbody>
    </table></div>
    <c:if test="${empty cateList}"><p class="text-secondary">No categories found.</p></c:if>
</section>
</body></html>
