package listener;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import repositories.*;
import service.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CustomContextListener implements ServletContextListener {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Linguo";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setUrl(DB_URL);

        UserRepository userRepository = new UserRepositoryJdbcImpl(dataSource);
        TopicRepository topicRepository = new TopicRepositoryJdbcImpl(dataSource);
        QuestionRepository questionRepository = new QuestionRepositoryJdbcImpl(dataSource);
        AnswerRepository answerRepository = new AnswerRepositoryJdbcImpl(dataSource);
        RatingRepository ratingRepository = new RatingRepositoryJdbcImpl(dataSource);

        SignInService signInService = new SignInServiceImpl(userRepository);
        servletContext.setAttribute("signInService", signInService);

        SignUpService signUpService = new SignUpServiceImpl(userRepository);
        servletContext.setAttribute("signUpService", signUpService);

        ProfileService profileService = new ProfileServiceImpl(userRepository);
        servletContext.setAttribute("profileService", profileService);

        QuestionService questionService = new QuestionServiceImpl(userRepository, questionRepository, topicRepository);
        servletContext.setAttribute("questionService", questionService);

        AnswerService answerService = new AnswerServiceImpl(userRepository, answerRepository, ratingRepository);
        servletContext.setAttribute("answerService", answerService);

        TopicService topicService = new TopicServiceImpl(topicRepository, questionRepository);
        servletContext.setAttribute("topicService", topicService);

        RatingService ratingService = new RatingServiceImpl(answerRepository, ratingRepository);
        servletContext.setAttribute("ratingService", ratingService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
