package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.user.RegistrationRequest;
import itmo.blps.elearningplatform.dto.user.UserDto;
import itmo.blps.elearningplatform.mapper.UserMapper;
import itmo.blps.elearningplatform.model.user.Role;
import itmo.blps.elearningplatform.model.user.User;
import itmo.blps.elearningplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserDto> getAllUsers(Pageable pageable, Role role) {
        if (role == null) {
            return userRepository.findAll(pageable).map(userMapper::toDto);
        } else {
            return userRepository.findAllByRole(role, pageable).map(userMapper::toDto);
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

    public Page<User> findAllDisabledUsers(Pageable pageable) {
        return userRepository.findAllByEnabledFalse(pageable);
    }

    public boolean isOwnerRegistered() {
        return userRepository.existsByRole(Role.ROLE_OWNER);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User createUser(RegistrationRequest request, Role role, boolean enabled) {
        validateUsername(request.username());
        return userRepository.save(toUser(request, role, enabled));
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is taken: " + username);
        }
    }

    private User toUser(RegistrationRequest request, Role role, boolean enabled) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setEnabled(enabled);
        return user;
    }
}
