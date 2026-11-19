<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Reset Password</title></head>
<body>
<section class="auth-card page-card">
        <h1 class="h3 mb-2">Reset password</h1>
        <p class="text-secondary">Enter the code from your email.</p>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/reset-password" data-validate novalidate>
            <div class="mb-3"><label class="form-label" for="email">Email address</label>
                <input class="form-control" id="email" name="email" type="email" maxlength="255"
                       value="<c:out value='${param.email}'/>" required>
                <div class="invalid-feedback">Enter your email.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="code">OTP code</label>
                <input class="form-control" id="code" type="text" inputmode="numeric" name="code" maxlength="6" pattern="[0-9]{6}" required>
                <div class="invalid-feedback">Enter the six-digit code.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="password">New password</label>
                <input class="form-control" id="password" type="password" name="password" minlength="8" maxlength="128" required>
                <div class="invalid-feedback">Use 8-128 characters.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="confirm">Confirm password</label>
                <input class="form-control" id="confirm" type="password" name="confirm" minlength="8" maxlength="128" required>
                <div class="invalid-feedback">Passwords must match.</div>
            </div>
            <button class="btn btn-primary w-100" type="submit">Update password</button>
        </form>
    </section>
</body></html>
