package servlets;

import dto.ProfileDto;
import dto.QuestionDto;
import service.ProfileService;
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
import java.util.Optional;

@WebServlet("/userProfile")
public class UserProfileServlet extends HttpServlet {

    QuestionService questionService;
    ProfileService profileService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("profile_id"));
        System.out.println("userId: " + userId);

        try {
            Optional<ProfileDto> profileDto = profileService.getProfileById(userId);
            if (profileDto.isPresent()) {
                ProfileDto profile = profileDto.get();
                request.setAttribute("profile", profile);

                // Получаем список вопросов пользователя
                List<QuestionDto> questionDtos = questionService.findAllByUserId(userId);
                request.setAttribute("questionCount", questionDtos.size());
                request.setAttribute("questions", questionDtos);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("User not found");
                return;
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
            return;
        }

        // Передача данных в JSP
        request.getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.questionService = (QuestionService) config.getServletContext().getAttribute("questionService");
        this.profileService = (ProfileService) config.getServletContext().getAttribute("profileService");
        System.out.println("Initializing questionService in UserQuestionsServlet: " + questionService);
        if (questionService == null) {
            throw new IllegalStateException("questionService is not initialized");
        }
    }
}
