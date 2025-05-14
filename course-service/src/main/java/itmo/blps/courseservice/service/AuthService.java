package itmo.blps.courseservice.service;

import itmo.blps.courseservice.dto.user.AuthenticationRequest;
import itmo.blps.courseservice.dto.user.JwtResponse;
import itmo.blps.courseservice.dto.user.RegistrationRequest;
import itmo.blps.courseservice.dto.user.UserDto;
import itmo.blps.courseservice.mapper.UserMapper;
import itmo.blps.courseservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userService.getUserEntityByUsername(request.username());
        validateUserEnabled(user);
        return new JwtResponse(jwtService.generateToken(user), userMapper.toDto(user));
    }

    public JwtResponse registerOwner(RegistrationRequest request) {
        if (userService.isOwnerRegistered()) {
            throw new IllegalStateException("Owner is already registered");
        }
        boolean enabled = true;
        User user = userService.createUser(request, User.Role.ROLE_OWNER, enabled);
        return new JwtResponse(jwtService.generateToken(user), userMapper.toDto(user));
    }

    public JwtResponse registerStudent(RegistrationRequest request) {
        boolean enabled = true;
        User user = userService.createUser(request, User.Role.ROLE_STUDENT, enabled);
        return new JwtResponse(jwtService.generateToken(user), userMapper.toDto(user));
    }

    public UserDto applyAdminRegistrationRequest(RegistrationRequest request) {
        boolean enabled = false;
        User user = userService.createUser(request, User.Role.ROLE_ADMIN, enabled);
        return userMapper.toDto(user);
    }

    public UserDto applyTeacherRegistrationRequest(RegistrationRequest request) {
        boolean enabled = false;
        User user = userService.createUser(request, User.Role.ROLE_TEACHER, enabled);
        return userMapper.toDto(user);
    }

    public List<UserDto> getPendingRegistrationRequests() {
        return userService.getAllDisabledUserEntities().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto approveRegistrationRequest(Integer userId) {
        User user = userService.getEntityUserById(userId);
        user.setEnabled(true);
        userService.updateUser(user);
        return userMapper.toDto(user);
    }

    public void rejectRegistrationRequest(Integer userId) {
        User user = userService.getEntityUserById(userId);
        validateUserNotEnabled(user);
        userService.deleteUser(user);
    }

    private void validateUserEnabled(User user) {
        if (!user.isEnabled()) {
            throw new IllegalStateException("User is disabled: " + user.getUsername());
        }
    }

    private void validateUserNotEnabled(User user) {
        if (user.isEnabled()) {
            throw new IllegalStateException("User is enabled: " + user.getUsername());
        }
    }
}
