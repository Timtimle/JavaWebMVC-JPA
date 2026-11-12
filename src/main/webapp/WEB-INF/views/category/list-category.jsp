<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Category Management - JavaWebMVC</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/category.css">
</head>
<body>

<div class="container">
    <div class="card">

        <div class="header">
            <h1>Category Management</h1>

            <a class="btn btn-green"
               href="${pageContext.request.contextPath}/category/add">
                + Add Category
            </a>
        </div>

        <!-- Search categories by name -->
        <form class="search-form"
              action="${pageContext.request.contextPath}/category/list"
              method="get">

            <input type="text"
                   name="keyword"
                   value="<c:out value='${keyword}'/>"
                   placeholder="Search category name...">

            <button class="btn" type="submit">
                Search
            </button>

            <a class="btn btn-gray"
               href="${pageContext.request.contextPath}/category/list">
                Reset
            </a>
        </form>

        <!-- Category list -->
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Image</th>
                        <th>Category Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="cate" items="${cateList}">

                        <tr>
                            <td>
                                <c:out value="${cate.id}" />
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty cate.icon}">
                                        <c:url var="imageUrl"
                                               value="/category/image">
                                            <c:param name="name"
                                                     value="${cate.icon}" />
                                        </c:url>

                                        <img class="category-image"
                                             src="${imageUrl}"
                                             alt="Category image">
                                    </c:when>

                                    <c:otherwise>
                                        <span class="note">
                                            No image
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:out value="${cate.name}" />
                            </td>

                            <td>
                                <div class="actions">

                                    <c:url var="editUrl"
                                           value="/category/edit">
                                        <c:param name="id"
                                                 value="${cate.id}" />
                                    </c:url>

                                    <a class="btn"
                                       href="${editUrl}">
                                        Edit
                                    </a>

                                    <!-- Delete using POST -->
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/category/delete"
                                          onsubmit="return confirm('Delete this category?');">

                                        <input type="hidden"
                                               name="id"
                                               value="${cate.id}">

                                        <button class="btn btn-red"
                                                type="submit">
                                            Delete
                                        </button>
                                    </form>

                                </div>
                            </td>
                        </tr>

                    </c:forEach>

                    <c:if test="${empty cateList}">
                        <tr>
                            <td colspan="4" class="empty">
                                No categories found.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <p>
            <a href="${pageContext.request.contextPath}/waiting">
                Back to Dashboard
            </a>
        </p>

    </div>
</div>

</body>
</html>
