
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <div class="profile-page">
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
                            <div class="profile-info">
                                <h1>${profile.username}'s Profile</h1>
                                <p><strong>Language:</strong> ${profile.language}</p>
                                <div class="user-stats">
                                    <div>
                                        <span class="stat-value">${requestScope.questionCount}</span>
                                        <span class="stat-label">Questions</span>
                                    </div>
                                </div>
                            </div>
                            <div class="questions-list">
                                <h2>Questions by ${profile.username}</h2>
                                <c:forEach var="question" items="${questions}">
                                    <c:if test="${question != null}">
                                        <div class="user-question-item">
                                            <h3>${question.text}</h3>
                                            <p class="question-topic-name">Topic: ${question.topic}</p>
                                            <p class="created-at">Posted on: ${question.createdAt}</p>
                                            <a href="${pageContext.request.contextPath}/question?question_id=${question.questionId}" class="read-answers-link">Read Answers</a>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="user-not-found">
                                <h1>User not found</h1>
                                <p>Please check the user ID and try again.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
        </div>
    </main>

    <footer class="footer">
        <p>Linguo</p>
    </footer>

</div>
</body>

<style>
    <%@include file="/WEB-INF/styles/mainMessagesErrors.css"%>
    <%@include file="/WEB-INF/styles/userQuestions.css"%>
    <%@include file="/WEB-INF/styles/userProfile.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>
</html>
