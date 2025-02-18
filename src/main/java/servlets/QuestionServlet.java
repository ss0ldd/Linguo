package servlets;

import dto.AnswerDto;
import dto.QuestionDto;
import service.AnswerService;
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
import java.util.List;
import java.time.LocalDate;

@WebServlet("/question")
public class QuestionServlet extends HttpServlet {

    QuestionService questionService;
    AnswerService answerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.questionService = (QuestionService) config.getServletContext().getAttribute("questionService");
        this.answerService = (AnswerService) config.getServletContext().getAttribute("answerService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long question_id = Long.valueOf(request.getParameter("question_id"));

        try{
            QuestionDto questionDto = questionService.findById(question_id);

            List<AnswerDto> answerDtos = answerService.findByQuestionId(question_id);
            request.setAttribute("question", questionDto);
            request.setAttribute("answers", answerDtos);
            request.getRequestDispatcher("/WEB-INF/jsp/question.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
