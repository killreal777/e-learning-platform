package itmo.blps.courseservice.controller;

import itmo.blps.courseservice.dto.user.AuthenticationRequest;
import itmo.blps.courseservice.dto.user.JwtResponse;
import itmo.blps.courseservice.dto.user.RegistrationRequest;
import itmo.blps.courseservice.dto.user.UserDto;
import itmo.blps.courseservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register/owner")
    public ResponseEntity<JwtResponse> registerOwner(@RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerOwner(request));
    }

    @PostMapping("/register/student")
    public ResponseEntity<JwtResponse> registerStudent(@RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerStudent(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserDto> registerAdmin(@RequestBody RegistrationRequest request) {
        return ResponseEntity.accepted().body(authService.applyAdminRegistrationRequest(request));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<UserDto> registerTeacher(@RequestBody RegistrationRequest request) {
        return ResponseEntity.accepted().body(authService.applyTeacherRegistrationRequest(request));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/register/requests")
    public ResponseEntity<List<UserDto>> getPendingRegistrationRequests() {
        return ResponseEntity.ok(authService.getPendingRegistrationRequests());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PatchMapping("/register/requests/{userId}/approve")
    public ResponseEntity<UserDto> approveRegistrationApplication(@PathVariable Integer userId) {
        return ResponseEntity.ok(authService.approveRegistrationRequest(userId));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/register/requests/{userId}/reject")
    public ResponseEntity<Void> rejectRegistrationApplication(@PathVariable Integer userId) {
        authService.rejectRegistrationRequest(userId);
        return ResponseEntity.noContent().build();
    }
}
