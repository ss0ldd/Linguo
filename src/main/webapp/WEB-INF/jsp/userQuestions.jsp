<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Questions</title>
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

        <div class="topic-name">
            <h1>My Questions</h1>
        </div>

        <div class="questions-list">
            <c:forEach var="question" items="${questions}">
                <c:if test="${question != null}">
                    <div class="question-container">
                        <p class="question-topic-name">Topic: ${question.topic}</p>
                        <p class="user-name">Asked by: ${question.userName}</p>
                        <p class="question-text">${question.text}</p>
                        <p class="created-at">Posted on: ${question.createdAt}</p>
                        <form action="${pageContext.request.contextPath}/userQuestions" method="post">
                            <input type="hidden" name="question_id" value="${question.questionId}">
                            <button type="submit" class="delete-button">Delete question</button>
                        </form>
                        <a href="${pageContext.request.contextPath}/question?question_id=${question.questionId}" class="read-answers-link">Read Answers</a>
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
    <%@include file="/WEB-INF/styles/mainMessagesErrors.css"%>
    <%@include file="/WEB-INF/styles/userQuestions.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>

</html>