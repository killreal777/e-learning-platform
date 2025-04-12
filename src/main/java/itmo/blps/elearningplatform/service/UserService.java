package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.user.RegistrationRequest;
import itmo.blps.elearningplatform.dto.user.UserDto;
import itmo.blps.elearningplatform.mapper.UserMapper;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.repository.UserRepository;
import itmo.blps.elearningplatform.service.transaction.Transactional;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers(User.Role role) {
        if (role == null) {
            return userRepository.findAll().stream()
                    .map(userMapper::toDto)
                    .toList();
        } else {
            return userRepository.findAllByRole(role).stream()
                    .map(userMapper::toDto)
                    .toList();
        }
    }

    public UserDto getUserById(Integer id) {
        return userMapper.toDto(getEntityUserById(id));
    }

    public User getEntityUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public List<User> getAllDisabledUserEntities() {
        return userRepository.findAllByEnabledFalse();
    }

    public boolean isOwnerRegistered() {
        return userRepository.existsByRole(User.Role.ROLE_OWNER);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public User createUser(RegistrationRequest request, User.Role role, boolean enabled) {
        validateUsername(request.username());
        return userRepository.save(toUser(request, role, enabled));
    }

    public User getUserEntityByIdAndRole(Integer userId, User.Role role) {
        return userRepository.findByIdAndRole(userId, role)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User not found with ID: %s and role: %s", userId, role)
                ));
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is taken: " + username);
        }
    }

    private User toUser(RegistrationRequest request, User.Role role, boolean enabled) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setEnabled(enabled);
        return user;
    }
}
