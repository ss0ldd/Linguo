package servlets;

import dto.AnswerDto;
import dto.QuestionDto;
import service.AnswerService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/allAnswers")
public class AllAnswersServlet extends HttpServlet {
    AnswerService answerService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<AnswerDto> answerDtos = answerService.allAnswers();
            request.setAttribute("answers", answerDtos);
            request.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.answerService = (AnswerService) config.getServletContext().getAttribute("answerService");
    }
}
