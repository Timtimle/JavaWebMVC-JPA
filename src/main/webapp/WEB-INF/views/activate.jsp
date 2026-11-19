<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Activate Account</title></head>
<body>
<section class="auth-card page-card">
        <h1 class="h3 mb-2">Activate account</h1>
        <p class="text-secondary">Enter the OTP sent to your email.</p>
        <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
        <form action="${pageContext.request.contextPath}/activate" method="post" data-validate novalidate>
            <div class="mb-3"><label class="form-label" for="email">Email address</label>
                <input class="form-control" id="email" name="email" type="email" maxlength="255"
                       value="<c:out value='${empty email ? param.email : email}'/>" required>
                <div class="invalid-feedback">Enter a valid email address.</div>
            </div>
            <div class="mb-3"><label class="form-label" for="code">OTP code</label>
                <input class="form-control" id="code" name="code" type="text" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" required>
                <div class="invalid-feedback">Enter the six-digit code.</div>
            </div>
            <button class="btn btn-primary" type="submit">Verify</button>
            <button class="btn btn-outline-secondary ms-2" type="submit" name="action" value="resend" formnovalidate>Resend OTP</button>
        </form>
        <p class="small mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Back to login</a></p>
    </section>
</body></html>
