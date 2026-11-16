<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Forgot Password</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body><main class="form-card"><h1>Forgot Password</h1><p>Request a six-digit password reset code by email.</p>
<c:if test="${not empty alert}"><p class="notice error"><c:out value="${alert}"/></p></c:if>
<c:if test="${not empty info}"><p class="notice"><c:out value="${info}"/></p></c:if>
<form method="post" action="${pageContext.request.contextPath}/forgot-password"><label>Email</label><input type="email" name="email" required><button type="submit">Send reset code</button></form><p><a href="${pageContext.request.contextPath}/login">Back to login</a></p></main></body></html>
