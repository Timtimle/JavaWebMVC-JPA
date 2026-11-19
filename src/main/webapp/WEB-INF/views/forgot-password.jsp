<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Forgot Password</title></head>
<body>
<section class="auth-card page-card">
        <h1 class="h3 mb-2">Forgot password</h1>
        <p class="text-secondary">We'll send a six-digit code to your email.</p>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post" data-validate novalidate>
            <div class="mb-3"><label class="form-label" for="email">Email address</label>
                <input class="form-control" id="email" name="email" type="email" maxlength="255"
                       value="<c:out value='${email}'/>" required>
                <div class="invalid-feedback">Enter a valid email address.</div>
            </div>
            <button class="btn btn-primary w-100" type="submit">Send OTP</button>
        </form>
        <p class="small mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Back to login</a></p>
    </section>
</body></html>
