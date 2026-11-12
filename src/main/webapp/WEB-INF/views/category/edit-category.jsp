<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Edit Category - JavaWebMVC</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/category.css">
</head>
<body>

<div class="container">
    <div class="card form-card">

        <h2>Edit Category</h2>

        <!-- Update the selected category -->
        <form action="${pageContext.request.contextPath}/category/edit"
              method="post"
              enctype="multipart/form-data">

            <input type="hidden"
                   name="id"
                   value="${category.id}">

            <div class="form-group">
                <label for="catename">Category Name *</label>

                <input type="text"
                       id="catename"
                       name="catename"
                       value="<c:out value='${category.name}'/>"
                       maxlength="255"
                       required>
            </div>

            <div class="form-group">
                <label>Current Image</label>

                <c:choose>
                    <c:when test="${not empty category.icon}">
                        <c:url var="imageUrl"
                               value="/category/image">
                            <c:param name="name"
                                     value="${category.icon}" />
                        </c:url>

                        <img class="category-image"
                             src="${imageUrl}"
                             alt="Current category image">
                    </c:when>

                    <c:otherwise>
                        <span class="note">No image</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="form-group">
                <label for="icon">Replace Image</label>

                <input type="file"
                       id="icon"
                       name="icon"
                       accept="image/png,image/jpeg,image/gif">

                <span class="note">
                    Leave empty to keep the current image.
                    Maximum 5 MB.
                </span>
            </div>

            <div class="form-buttons">
                <button class="btn"
                        type="submit">
                    Save Changes
                </button>

                <a class="btn btn-gray"
                   href="${pageContext.request.contextPath}/category/list">
                    Cancel
                </a>
            </div>
        </form>

    </div>
</div>

</body>
</html>
