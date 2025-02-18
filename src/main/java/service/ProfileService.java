package service;


import dto.ProfileDto;

import java.sql.SQLException;
import java.util.Optional;

public interface ProfileService {
    Optional<ProfileDto> getProfileById(Long id) throws SQLException;
    Optional<ProfileDto> getProfileByUsername(String username) throws SQLException;
    void updateProfile(ProfileDto profileDto) throws SQLException;
    void deleteProfile(Long id) throws SQLException;
}
