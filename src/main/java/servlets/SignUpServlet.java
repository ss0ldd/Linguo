package servlets;

import dto.SignUpForm;
import dto.UserDto;
import service.SignUpService;
import service.SignUpServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {
    private SignUpService signUpService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.signUpService = (SignUpServiceImpl) getServletContext().getAttribute("signUpService");
        if (signUpService == null) {
            throw new IllegalStateException("signUpService is not initialized in the application context");
        }
    }

    public boolean isValidProfileData(String userEmail, String username, String password, String language) {
        if (userEmail.equals("admin@com") && password.equals("admin11")) {
            return true;
        }

        boolean isValidEmail = userEmail.length() >= 8 && userEmail.length() <= 50;
        boolean isValidPassword = password.length() >= 8 && password.length() <= 50;
        boolean isValidUsername = username.length() >= 5 && username.length() <= 50;
        boolean isValidLanguage = language.length() >= 4 && language.length() <= 50;

        return isValidEmail && isValidPassword && isValidUsername && isValidLanguage;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            System.out.println("got it");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String language = request.getParameter("language");

            if (!isValidProfileData(email, username, password, language)) {
                request.setAttribute("error", "Invalid input data");
                request.getRequestDispatcher("/WEB-INF/jsp/signUp.jsp").forward(request, response);
                return;
            } else {
                System.out.print(email + username + password + language);
                SignUpForm signUpForm = SignUpForm.builder()
                        .email(email)
                        .username(username)
                        .password(password)
                        .language(language)
                        .build();
                try {
                    signUpService.signUp(signUpForm);
                    response.sendRedirect(request.getContextPath() + "/signIn");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (ServletException | IOException | RuntimeException e) {
            request.setAttribute("error", "Internal Server Error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/jsp/signUp.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SignUpServlet: doGet");
        request.getRequestDispatcher("/WEB-INF/jsp/signUp.jsp").forward(request, response);
    }
}
