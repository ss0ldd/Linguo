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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/createAnswer")
public class CreateAnswer extends HttpServlet {
    AnswerService answerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.answerService = (AnswerService) config.getServletContext().getAttribute("answerService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String text = request.getParameter("text");
        String username = (String) session.getAttribute("username");
        Long user_id = (Long) session.getAttribute("user_id");
        String questionParam = request.getParameter("question_id");
        Long question_id = Long.valueOf(questionParam);
        LocalDate createdAt = LocalDate.now();

        try{
            if (text == null || text.trim().isEmpty()) {
                request.setAttribute("errorMessage", "You haven't entered anything.");
                return;
            }

            AnswerDto answerDto = AnswerDto.builder()
                    .questionId(question_id)
                    .username(username)
                    .userId(user_id)
                    .text(text)
                    .createdAt(Date.valueOf(createdAt))
                    .rating(0.0)
                    .build();

            answerService.saveAnswer(answerDto);
            response.sendRedirect(request.getContextPath() + "/question?question_id=" + questionParam);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
