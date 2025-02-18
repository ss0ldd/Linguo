<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Admin</title>
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

        <div class="answers-list">
            <c:forEach var="answer" items="${answers}">
                <c:if test="${answer != null}">
                    <div class="answer-container">
                        <p class="username">
                            <c:if test="${not empty answer.userId && not empty answer.username}">
                                <a href="${pageContext.request.contextPath}/userProfile?profile_id=${answer.userId}">
                                        ${answer.username}
                                </a>
                            </c:if>
                            <c:if test="${empty answer.userId || empty answer.username}">
                                Unknown User
                            </c:if>
                        </p>

                        <p class="answer-text">${answer.text}</p>
                        <p class="created-at">${answer.createdAt}</p>
                        <p class="rating">Rating: <fmt:formatNumber value="${answer.rating}" maxFractionDigits="1" /></p>

                    </div>
                </c:if>
            </c:forEach>
        </div>
    </main>

    <footer class="footer">
        <p>Linguo</p>
    </footer>
</div>
</body>

<style>
    <%@include file="/WEB-INF/styles/adminPage.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>

</html>
