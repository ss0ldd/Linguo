package servlets;

import dto.AnswerDto;
import models.Rating;
import service.RatingService;

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

@WebServlet("/rating")
public class RatingServlet extends HttpServlet {

    RatingService ratingService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long answer_id = Long.valueOf(request.getParameter("answer_id"));
        Long user_id = (Long) session.getAttribute("user_id");
        String questionParam = request.getParameter("question_id");
        int value = Integer.parseInt(request.getParameter("rating"));

        try{
            if (value == 0) {
                return;
            }

            Rating rating = Rating.builder()
                    .answer_id(answer_id)
                    .user_id(user_id)
                    .value(value)
                    .build();

            System.out.println(rating);
            ratingService.updateOrSaveRating(rating);
            response.sendRedirect(request.getContextPath() + "/question?question_id=" + questionParam);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ratingService = (RatingService) config.getServletContext().getAttribute("ratingService");
    }
}
