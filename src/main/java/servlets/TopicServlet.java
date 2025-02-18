package servlets;

import dto.AnswerDto;
import dto.QuestionDto;
import dto.TopicDto;
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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/topic")
public class TopicServlet extends HttpServlet {
    QuestionService questionService;
    TopicService topicService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String topic_name = request.getParameter("topic_name");

        try{
            Optional<Topic> topic = topicService.findByName(topic_name);

            if (topic.isPresent()) {
                Topic topicObj = topic.get();
                List<QuestionDto> questionDtos = questionService.findAllByTopicId(topicObj.getTopicId());

                request.setAttribute("topicName", topicObj.getTopicName());
                request.setAttribute("questionDtos", questionDtos);
                request.getRequestDispatcher("/WEB-INF/jsp/topicQuestions.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.topicService = (TopicService) config.getServletContext().getAttribute("topicService");
        this.questionService = (QuestionService) config.getServletContext().getAttribute("questionService");
    }
}
