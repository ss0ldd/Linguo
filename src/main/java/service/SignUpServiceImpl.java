package service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import dto.SignUpForm;
import models.User;
import repositories.UserRepository;

import java.sql.SQLException;

public class SignUpServiceImpl implements SignUpService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SignUpServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void signUp(SignUpForm form) throws SQLException {
        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .language(form.getLanguage()).build();
        System.out.println("signUpServiceImpl");
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameEmailExists(String username, String email) throws SQLException {
        try{
            return userRepository.isEmailOrUsernameExists(username, email);
        } catch (SQLException e) {
            return false;
        }
    }
}
