<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Main Page</title>
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

        <div class="create-topic-container">
            <h2>Create Topic</h2>
            <form action="${pageContext.request.contextPath}/createTopic" method="POST">
                <label for="text">Text:</label>
                <input type="text" id="text" name="text" required>

                <button type="submit">Create</button>
            </form>
        </div>

        <div class="search-topic-container">
            <h2>Search Topic</h2>
            <form action="${pageContext.request.contextPath}/mainPage" method="POST">
                <label for="topicName">Topic:</label>
                <input type="text" id="topicName" name="topicName" required>

                <button type="submit">Find</button>
            </form>
        </div>
    </main>

    <footer class="footer">
        <p>Linguo</p>
    </footer>
</div>

</body>

<style>
    <%@include file="/WEB-INF/styles/mainMessagesErrors.css"%>
    <%@include file="/WEB-INF/styles/mainPage.css"%>
    <%@include file="/WEB-INF/styles/header.css"%>
    <%@include file="/WEB-INF/styles/footer.css"%>
</style>

</html>