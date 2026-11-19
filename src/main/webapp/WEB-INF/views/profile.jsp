<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>My Profile</title></head>
<body>
<div class="row g-4">
        <div class="col-lg-7">
            <section class="page-card p-4">
                <h1 class="h3 mb-3">My profile</h1>
                <c:if test="${not empty alert}"><div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div></c:if>
<c:if test="${not empty info}"><div class="alert alert-success" role="status"><c:out value="${info}"/></div></c:if>
                <div class="d-flex align-items-center gap-3 mb-4">
                    <c:choose>
                        <c:when test="${not empty user.avatar}"><img class="avatar" src="<c:out value='${user.avatar}'/>" alt="Profile image"></c:when>
                        <c:otherwise><div class="avatar d-flex align-items-center justify-content-center text-secondary">No photo</div></c:otherwise>
                    </c:choose>
                    <div><p class="fw-semibold mb-1"><c:out value="${user.username}"/></p>
                    <p class="text-secondary small mb-0">Upload a JPG, PNG or GIF (max 5 MB).</p></div>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data" data-validate novalidate>
                    <div class="mb-3"><label class="form-label" for="fullname">Full name</label>
                        <input class="form-control" id="fullname" name="fullname" type="text" maxlength="255" required
                               value="<c:out value='${user.fullName}'/>">
                        <div class="invalid-feedback">Full name is required.</div>
                    </div>
                    <div class="mb-3"><label class="form-label" for="phone">Phone</label>
                        <input class="form-control" id="phone" name="phone" type="tel" maxlength="20" pattern="[+0-9 ()-]{8,20}"
                               value="<c:out value='${user.phone}'/>">
                        <div class="invalid-feedback">Enter a valid phone or leave blank.</div>
                    </div>
                    <div class="mb-3"><label class="form-label" for="email">Email</label>
                        <input class="form-control" id="email" name="email" type="email" maxlength="255" required
                               value="<c:out value='${user.email}'/>">
                        <div class="form-text">Changing your email sends an OTP to the new address.</div>
                        <div class="invalid-feedback">Enter a valid email address.</div>
                    </div>
                    <div class="mb-3"><label class="form-label" for="image">Profile image</label>
                        <input class="form-control" id="image" name="image" type="file" accept="image/jpeg,image/png,image/gif,.jpg,.jpeg,.png,.gif">
                    </div>
                    <button class="btn btn-primary" type="submit">Save profile</button>
                </form>
            </section>
        </div>
        <div class="col-lg-5">
            <section class="page-card p-4">
                <h2 class="h5 mb-3">Verify new email</h2>
                <c:choose>
                <c:when test="${not empty pendingEmail}">
                    <p class="small">A six-digit code was sent to <strong><c:out value="${pendingEmail}"/></strong>.</p>
                    <form method="post" action="${pageContext.request.contextPath}/profile/verify-email" data-validate novalidate>
                        <div class="mb-3"><label class="form-label" for="code">OTP</label>
                            <input class="form-control" id="code" name="code" type="text" maxlength="6" pattern="[0-9]{6}" inputmode="numeric" required>
                            <div class="invalid-feedback">Enter a six-digit code.</div>
                        </div>
                        <button class="btn btn-success" type="submit">Confirm email</button>
                    </form>
                    <p class="small text-secondary mt-3 mb-0">The code expires after 10 minutes. Save the form again to request another code after 60 seconds.</p>
                </c:when>
                <c:otherwise><p class="text-secondary mb-0">Your email address is verified. Enter a different email to request a change.</p></c:otherwise>
                </c:choose>
            </section>
        </div>
    </div>
</body></html>
