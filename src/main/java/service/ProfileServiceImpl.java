package service;

import dto.ProfileDto;
import models.User;
import org.springframework.context.annotation.Profile;
import repositories.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class ProfileServiceImpl implements ProfileService{

    private final UserRepository userRepository;

    public ProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<ProfileDto> profileDtoBuilder(Optional<User> userOptional){
        return userOptional.map(user -> ProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .language(user.getLanguage())
                .build());
    }

    @Override
    public Optional<ProfileDto> getProfileById(Long id) throws SQLException {
        return profileDtoBuilder(userRepository.findById(id));
    }

    @Override
    public Optional<ProfileDto> getProfileByUsername(String username) throws SQLException {
        return profileDtoBuilder(userRepository.findByUsername(username));
    }

    @Override
    public void updateProfile(ProfileDto profileDto) throws SQLException {
        if (profileDto == null || profileDto.getId() == null) {
            throw new IllegalArgumentException("Invalid ProfileDto: missing ID");
        }

        Long userId = profileDto.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        User user = userOptional.get();
        user.setUsername(profileDto.getUsername());
        user.setEmail(profileDto.getEmail());
        user.setLanguage(profileDto.getLanguage());
        userRepository.update(user);
    }

    @Override
    public void deleteProfile(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("Invalid ProfileDto: missing ID");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }
        userRepository.removeById(id);
    }
}
