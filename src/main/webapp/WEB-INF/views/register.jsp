<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Register</title></head>
<body>
<section class="auth-card page-card">
        <h1 class="h3 fw-bold mb-2">Create account</h1>
        <p class="text-secondary mb-4">Verify your email to activate your account.</p>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <form action="${pageContext.request.contextPath}/register" method="post" data-validate novalidate>
            <div class="mb-3"><label class="form-label" for="username">Username</label>
                <input class="form-control" id="username" type="text" name="username" value="<c:out value='${username}'/>"
                       minlength="3" maxlength="100" pattern="[A-Za-z0-9_.-]{3,100}" autocomplete="username" required>
                <div class="invalid-feedback">Use 3-100 letters, numbers, dots, dashes or underscores.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="email">Email</label>
                <input class="form-control" id="email" type="email" name="email" maxlength="255" value="<c:out value='${email}'/>" required>
                <div class="invalid-feedback">Enter a valid email.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="fullname">Full name</label>
                <input class="form-control" id="fullname" type="text" name="fullname" maxlength="255" value="<c:out value='${fullname}'/>">
            </div>
            <div class="mb-3"><label class="form-label" for="phone">Phone</label>
                <input class="form-control" id="phone" type="tel" name="phone" maxlength="20" pattern="[+0-9 ()-]{8,20}" value="<c:out value='${phone}'/>">
                <div class="invalid-feedback">Enter a valid phone number or leave blank.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="password">Password</label>
                <input class="form-control" id="password" type="password" name="password" minlength="8" maxlength="128" required autocomplete="new-password">
                <div class="invalid-feedback">Use 8-128 characters.</div>
            </div>
            <button class="btn btn-primary w-100" type="submit">Register</button>
        </form>
        <p class="small mt-3 mb-0">Already registered? <a href="${pageContext.request.contextPath}/login">Login</a></p>
    </section>
</body></html>
