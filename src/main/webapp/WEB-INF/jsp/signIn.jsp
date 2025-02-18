<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sign In</title>
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
                    <li><a href="${pageContext.request.contextPath}/logout">Log out</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="main-content">
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

        <div class="signin-container">
            <h2>Sign in to your profile</h2>
            <form action="${pageContext.request.contextPath}/signIn" method="POST">
                <label for="email">Email:</label>
                <input type="text" id="email" name="email" required>

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>

                <button type="submit">Sign In</button>
            </form>

            <p>Don't have an account? <a href="${pageContext.request.contextPath}/signUp">Sign up for free</a></p>
        </div>
    </main>

    <footer class="footer">
        <p>Linguo</p>
    </footer>
</div>

</body>

<style>
    <%@include file="/WEB-INF/styles/mainMessagesErrors.css"%>
    <%@include file="/WEB-INF/styles/signIn.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>

</html>