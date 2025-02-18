package servlets;

import dto.QuestionDto;
import service.QuestionService;

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

@WebServlet("/createQuestion")
public class CreateQuestionServlet extends HttpServlet {
    QuestionService questionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.questionService = (QuestionService) this.getServletContext().getAttribute("questionService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String text = request.getParameter("text");
        String username = (String) session.getAttribute("username");
        Long user_id = (Long) session.getAttribute("user_id");
        String topic = request.getParameter("topicName");
        LocalDate createdAt = LocalDate.now();

        try{
            if (text == null || text.trim().isEmpty() || topic == null || topic.trim().isEmpty()) {
                request.setAttribute("errorMessage", "You haven't entered anything.");
                return;
            }

            QuestionDto questionDto = QuestionDto.builder()
                    .userName(username)
                    .userId(user_id)
                    .text(text)
                    .createdAt(Date.valueOf(createdAt))
                    .topic(topic)
                    .build();

            questionService.save(questionDto);
            response.sendRedirect(request.getContextPath() + "/topic?topic_name=" + topic);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
