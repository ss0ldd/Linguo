package servlets;

import dto.AnswerDto;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    ProfileService profileService;
    QuestionService questionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.profileService = (ProfileService) getServletContext().getAttribute("profileService");
        this.questionService = (QuestionService) getServletContext().getAttribute("questionService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try{
            Long userId = (Long) session.getAttribute("user_id");

            Optional<ProfileDto> profileDto = profileService.getProfileById(userId);
            if (profileDto.isPresent()) {
                ProfileDto profile = profileDto.get();
                request.setAttribute("profile", profile);
                List<QuestionDto> questionDtos = questionService.findAllByUserId(userId);

                request.setAttribute("questions", questionDtos);
                request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("User not found");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
        }
    }

    public boolean isValidProfileData(String username, String language) {
        return username.length() >= 3 && username.length() <= 50 && language.length() >= 2 && language.length() <= 50;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try{
            System.out.println("Entered profileServlet");
            Long userId = (Long) session.getAttribute("user_id");
            System.out.println(userId);

            String username = request.getParameter("username");
            String language = request.getParameter("language");

            if (!isValidProfileData(username, language)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid username or language");
                return;
            }

            Optional<ProfileDto> profileDto = profileService.getProfileById(userId);
            if (profileDto.isPresent()) {
                ProfileDto profile = profileDto.get();
                profile.setUsername(username);
                profile.setLanguage(language);
                profileService.updateProfile(profile);
                session.setAttribute("message", "Profile updated successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("User not found");
            }

            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
        }
    }
}
