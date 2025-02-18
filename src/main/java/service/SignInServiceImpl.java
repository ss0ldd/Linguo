package service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import dto.SignInForm;
import dto.UserDto;
import models.User;
import repositories.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class SignInServiceImpl implements SignInService{

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public SignInServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public UserDto signIn(SignInForm form) throws SQLException {
        Optional<User> userOptional = userRepository.findByEmail(form.getEmail());
        System.out.println("signIn: " + userOptional.isPresent());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user.getPassword());
            System.out.println(user.getEmail());
            if (passwordChecking(form.getPassword(), user.getPassword())){
                return UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .build();
            }
        }
        return null;
    }

    @Override
    public Boolean passwordChecking(String password, String encodedPassword) {
        System.out.println(passwordEncoder.matches(password, encodedPassword));
        return passwordEncoder.matches(password, encodedPassword);
    }
}
