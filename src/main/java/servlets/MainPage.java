package servlets;

import dto.QuestionDto;
import models.Topic;
import service.QuestionService;
import service.TopicService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/mainPage")
public class MainPage extends HttpServlet {
    TopicService topicService;
    QuestionService questionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.topicService = (TopicService) getServletContext().getAttribute("topicService");
        this.questionService = (QuestionService) getServletContext().getAttribute("questionService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String topicName = request.getParameter("topicName");

        try {
            Optional<Topic> topic = topicService.findByName(topicName);
            if (topic.isPresent()){
                response.sendRedirect(request.getContextPath() + "/topic?topic_name=" + topicName);
            } else {
                request.setAttribute("message", "Topic was not found");
                request.getRequestDispatcher("/WEB-INF/jsp/mainPage.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/mainPage.jsp").forward(request, response);
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("user_id");
        List<QuestionDto> questionDtos = null;
        try {
            questionDtos = questionService.findAllByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("questionDtos", questionDtos);
    }
}
