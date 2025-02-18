<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
<div class="page-container">

    <header class="header">
        <div class="header">
            <nav class="nav-bar">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/mainPage">Main page</a></li>
                    <li><a href="${pageContext.request.contextPath}/userQuestions?profile_id=${sessionScope.user_id}">My questions</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile">My profile</a></li>
                    <c:if test="${sessionScope.role == 'admin'}">
                        <li><a href="${pageContext.request.contextPath}/allAnswers">Admin</a></li>
                    </c:if>
                    <li><a href="${pageContext.request.contextPath}/logout">Log out</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main>
        <div class="messages">
            <c:if test="${not empty sessionScope.message}">
                <p class="message">${sessionScope.message}</p>
                <c:remove var="message" scope="session"/>
            </c:if>
        </div>

        <div class="error">
            <c:if test="${not empty param.error}">
                <p class="error">${param.error}</p>
            </c:if>
        </div>

        <div class="profile-container">
            <c:choose>
                <c:when test="${not empty requestScope.profile}">

                    <h1>User Profile</h1>

                    <div class="user-info-container">
                        <p><strong>Username:</strong> ${profile.username}</p>
                        <p><strong>Email:</strong> ${profile.email}</p>
                        <c:if test="${sessionScope.role == 'admin'}">
                            <li><a href="${pageContext.request.contextPath}/allAnswers">Admin</a></li>
                        </c:if>
                        <p><strong>Language:</strong> ${profile.language}</p>
                    </div>

                    <div class="update-profile-container">
                        <h2>Update Profile</h2>
                        <form action="${pageContext.request.contextPath}/profile" method="post">
                            <label for="username">Username:</label>
                            <input type="text" id="username" name="username" value="${profile.username}" required minlength="3" maxlength="50">

                            <label for="language">Language:</label>
                            <input type="text" id="language" name="language" value="${profile.language}" required minlength="2" maxlength="50">

                            <button type="submit">Update</button>
                        </form>
                    </div>

                </c:when>

                <c:otherwise>
                    <div class="user-not-found">
                        <h1>User not found</h1>
                        <p>Please <a href="${pageContext.request.contextPath}/login">log in</a> to view your profile.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </main>

    <footer class="footer">
        <p>Linguo</p>
    </footer>
</div>



</body>

<style>
    <%@include file="/WEB-INF/styles/mainMessagesErrors.css"%>
    <%@include file="/WEB-INF/styles/profile.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>

</html>
