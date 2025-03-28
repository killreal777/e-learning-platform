package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.user.AuthenticationRequest;
import itmo.blps.elearningplatform.dto.user.JwtResponse;
import itmo.blps.elearningplatform.dto.user.RegistrationRequest;
import itmo.blps.elearningplatform.dto.user.UserDto;
import itmo.blps.elearningplatform.mapper.UserMapper;
import itmo.blps.elearningplatform.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

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

    public void applyAdminRegistrationRequest(RegistrationRequest request) {
        boolean enabled = false;
        userService.createUser(request, User.Role.ROLE_ADMIN, enabled);
    }

    public void applyTeacherRegistrationRequest(RegistrationRequest request) {
        boolean enabled = false;
        userService.createUser(request, User.Role.ROLE_TEACHER, enabled);
    }

    public Page<UserDto> getPendingRegistrationRequests(Pageable pageable) {
        return userService.getAllDisabledUserEntities(pageable).map(userMapper::toDto);
    }

    public void approveRegistrationRequest(Integer userId) {
        User user = userService.getEntityUserById(userId);
        user.setEnabled(true);
        userService.updateUser(user);
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
