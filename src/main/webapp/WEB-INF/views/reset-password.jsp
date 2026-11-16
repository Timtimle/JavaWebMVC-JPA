<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Reset Password</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body><main class="form-card"><h1>Reset Password</h1><p>Enter your email verification code and a new password.</p>
<c:if test="${not empty alert}"><p class="notice error"><c:out value="${alert}"/></p></c:if>
<c:if test="${not empty info}"><p class="notice"><c:out value="${info}"/></p></c:if>
<form method="post" action="${pageContext.request.contextPath}/reset-password"><label>Email</label><input type="email" name="email" value="<c:out value='${param.email}'/>" required><label>Verification Code</label><input type="text" name="code" pattern="[0-9]{6}" maxlength="6" required><label>New password</label><input type="password" name="password" minlength="8" required><label>Confirm password</label><input type="password" name="confirm" minlength="8" required><button type="submit">Reset password</button></form><p><a href="${pageContext.request.contextPath}/login">Back to login</a></p></main></body></html>
