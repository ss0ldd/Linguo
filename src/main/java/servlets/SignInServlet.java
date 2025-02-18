package servlets;

import dto.SignInForm;
import dto.UserDto;
import service.SignInService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {
    private SignInService signInService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.signInService = (SignInService) getServletContext().getAttribute("signInService");
        if (this.signInService == null) {
            throw new IllegalStateException("signInService is not initialized in the application context");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("SignInServlet: doGet called");
        request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, resp);
    }

    public boolean isValidProfileData(String userEmail, String password) {
        if (userEmail.equals("admin@com") && password.equals("admin11")) {
            return true;
        }
        return userEmail.length() >= 8 && userEmail.length() <= 50 && password.length() >= 8 && password.length() <= 50;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (!isValidProfileData(email, password)) {
                request.setAttribute("error", "Invalid length of email or password");
                request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
                return;
            }

            SignInForm signInForm = SignInForm.builder()
                    .email(email)
                    .password(password)
                    .build();
            try {
                UserDto userDto = signInService.signIn(signInForm);
                if (userDto != null) {
                    HttpSession session = request.getSession(true);
                    if (email.equals("admin@com") && password.equals("admin11")) {
                        session.setAttribute("role", "admin");
                        userDto.setRole("admin");
                    }
                    System.out.println(session.getAttribute("role"));
                    session.setAttribute("authenticated", true);
                    session.setAttribute("user_id", userDto.getId());
                    session.setAttribute("username", userDto.getUsername());
                    session.setAttribute("role", userDto.getRole());

                    session.setAttribute("message", "You have successfully signed in!");

                    response.sendRedirect(request.getContextPath() + "/profile");
                } else {
                    request.setAttribute("error", "The email or password was entered incorrectly");
                    request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | ServletException | RuntimeException e) {
            request.setAttribute("error", "Internal Server Error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
        }
    }
}
