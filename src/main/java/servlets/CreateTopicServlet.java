package servlets;

import dto.TopicDto;
import models.Topic;
import service.TopicService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/createTopic")
public class CreateTopicServlet extends HttpServlet {
    TopicService topicService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        this.topicService = (TopicService) config.getServletContext().getAttribute("topicService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = request.getParameter("text");

        try{
            if (text == null || text.trim().isEmpty()) {
                request.setAttribute("message", "You haven't entered anything.");
                return;
            }

            try {
                Optional<Topic> topicOptional= topicService.findByName(text);
                if (topicOptional.isPresent()) {
                    throw new IllegalArgumentException("Topic " + text + " already exists.");

                } else{
                    TopicDto topicDto = TopicDto.builder()
                            .topicName(text)
                            .build();
                    topicService.save(topicDto);
                    response.sendRedirect(request.getContextPath() + "/topic?topic_name=" + topicDto.getTopicName());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
