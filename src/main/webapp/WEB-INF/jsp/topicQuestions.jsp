<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Topic Questions</title>
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

        <a href="${pageContext.request.contextPath}/mainPage" class="back-link">← Back to Main Page</a>

        <div class="topic-name">
            <p>${requestScope.topicName}</p>
        </div>

        <div class="ask-question-container">
            <h2>Ask a Question</h2>
            <form action="${pageContext.request.contextPath}/createQuestion?topicName=${requestScope.topicName}" method="POST">
                <label for="text">Text:</label>
                <input type="text" id="text" name="text" required>

                <button type="submit">Send</button>
            </form>
        </div>

        <div class="questions-list">
            <c:forEach var="question" items="${questionDtos}">
                <c:if test="${question != null}">
                    <div class="question-container">
                        <p class="username">
                            <c:if test="${not empty question.userId && not empty question.userName}">
                                <a href="${pageContext.request.contextPath}/userProfile?profile_id=${question.userId}">
                                        ${question.userName}
                                </a>
                            </c:if>
                            <c:if test="${empty question.userId || empty question.userName}">
                                Unknown User
                            </c:if>
                        </p>
                        <p class="question-text">${question.text}</p>
                        <p class="created-at">${question.createdAt}</p>
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
    <%@include file="/WEB-INF/styles/topicQuestions.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>

</html>