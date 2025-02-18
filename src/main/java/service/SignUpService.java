package service;

import dto.SignUpForm;

import java.sql.SQLException;

public interface SignUpService {
    void signUp(SignUpForm form) throws SQLException;
    boolean isUsernameEmailExists(String username, String email) throws SQLException;
}
