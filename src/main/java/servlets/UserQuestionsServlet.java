package servlets;

import dto.QuestionDto;
import service.QuestionService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/userQuestions")
public class UserQuestionsServlet extends HttpServlet {
    QuestionService questionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("profile_id"));
        System.out.println("userId" + userId);
        if (userId != null) {
            try{
                System.out.println("userId try" + userId);
                List<QuestionDto> questions = questionService.findAllByUserId(userId);
                System.out.println(questions);
                request.setAttribute("questions", questions);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        request.getRequestDispatcher("/WEB-INF/jsp/userQuestions.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String questionIdParam = request.getParameter("question_id");
        if (questionIdParam == null || questionIdParam.isEmpty()) {
            request.setAttribute("error", "Invalid question ID");
            request.getRequestDispatcher("/WEB-INF/jsp/userQuestions.jsp").forward(request, response);
            return;
        }

        try {
            Long questionId = Long.parseLong(questionIdParam);

            questionService.removeById(questionId);
            request.getSession().setAttribute("message", "Question deleted successfully");
            response.sendRedirect(request.getContextPath() + "/userQuestions?profile_id=" + request.getSession().getAttribute("user_id"));

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid question ID format");
            request.getRequestDispatcher("/WEB-INF/jsp/userQuestions.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete question with ID: " + questionIdParam, e);
        }
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.questionService = (QuestionService) config.getServletContext().getAttribute("questionService");
        System.out.println("Initializing questionService in UserQuestionsServlet: " + questionService);
        if (questionService == null) {
            throw new IllegalStateException("questionService is not initialized");
        }
    }
}
