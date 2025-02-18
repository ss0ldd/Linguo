package service;

import dto.SignInForm;
import dto.UserDto;

import java.sql.SQLException;

public interface SignInService {
    UserDto signIn(SignInForm form) throws SQLException;
    Boolean passwordChecking(String password, String encodedPassword);
}
