<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Activate Account</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head><body><main class="form-card"><h1>Activate Account</h1><p>Enter the six-digit verification code sent to your email.</p>
<c:if test="${not empty alert}"><p class="notice error"><c:out value="${alert}"/></p></c:if>
<c:if test="${not empty info}"><p class="notice"><c:out value="${info}"/></p></c:if>
<form method="post" action="${pageContext.request.contextPath}/activate"><label>Email</label><input type="email" name="email" value="<c:out value='${empty email ? param.email : email}'/>" required><label>Verification Code</label><input type="text" name="code" pattern="[0-9]{6}" maxlength="6" required><button type="submit">Activate account</button><button type="submit" name="action" value="resend" formnovalidate>Resend code</button></form><p><a href="${pageContext.request.contextPath}/login">Back to login</a></p></main></body></html>
