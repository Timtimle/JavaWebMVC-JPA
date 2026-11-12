<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Add Category - JavaWebMVC</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/category.css">
</head>
<body>

<div class="container">
    <div class="card form-card">

        <h2>Add New Category</h2>

        <!-- Create a new category -->
        <form action="${pageContext.request.contextPath}/category/add"
              method="post"
              enctype="multipart/form-data">

            <div class="form-group">
                <label for="catename">Category Name *</label>

                <input type="text"
                       id="catename"
                       name="catename"
                       maxlength="255"
                       placeholder="Enter category name"
                       required>
            </div>

            <div class="form-group">
                <label for="icon">Category Image</label>

                <input type="file"
                       id="icon"
                       name="icon"
                       accept="image/png,image/jpeg,image/gif">

                <span class="note">
                    JPG, PNG or GIF. Maximum 5 MB.
                </span>
            </div>

            <div class="form-buttons">
                <button class="btn btn-green"
                        type="submit">
                    Add Category
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
