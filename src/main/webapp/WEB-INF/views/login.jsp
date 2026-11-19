<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Login</title></head>
<body>
<section class="auth-card page-card">
        <h1 class="h3 fw-bold mb-2">Welcome back</h1>
        <p class="text-secondary mb-4">Sign in to your account.</p>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <c:if test="${param.activated eq '1'}"><div class="alert alert-success">Account activated.</div></c:if>
        <c:if test="${param.reset eq '1'}"><div class="alert alert-success">Password updated.</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/login" data-validate novalidate>
            <div class="mb-3">
                <label class="form-label" for="username">Username</label>
                <input class="form-control" id="username" type="text" name="username" maxlength="100" required
                       autocomplete="username" value="<c:out value='${rememberedUsername}'/>">
                <div class="invalid-feedback">Enter your username.</div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">Password</label>
                <input class="form-control" id="password" type="password" name="password" maxlength="128" required autocomplete="current-password">
                <div class="invalid-feedback">Enter your password.</div>
            </div>
            <div class="form-check mb-3"><input class="form-check-input" type="checkbox" name="remember" id="remember">
                <label class="form-check-label" for="remember">Remember me</label></div>
            <button class="btn btn-primary w-100" type="submit">Login</button>
        </form>
        <div class="d-flex justify-content-between mt-3 gap-2 flex-wrap small">
            <a href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a>
            <a href="${pageContext.request.contextPath}/register">Create an account</a>
            <a href="${pageContext.request.contextPath}/activate">Activate account</a>
        </div>
    </section>
</body></html>
