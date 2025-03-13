package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.user.AuthenticationRequest;
import itmo.blps.elearningplatform.dto.user.JwtResponse;
import itmo.blps.elearningplatform.dto.user.RegistrationRequest;
import itmo.blps.elearningplatform.dto.user.UserDto;
import itmo.blps.elearningplatform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> registerAdmin(@RequestBody RegistrationRequest request) {
        authService.applyAdminRegistrationRequest(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<Void> registerTeacher(@RequestBody RegistrationRequest request) {
        authService.applyTeacherRegistrationRequest(request);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/register/requests")
    public ResponseEntity<Page<UserDto>> getPendingRegistrationRequests(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(authService.getPendingRegistrationRequests(pageable));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PatchMapping("/register/requests/{userId}/approve")
    public ResponseEntity<Void> approveRegistrationApplication(@PathVariable Integer userId) {
        authService.approveRegistrationRequest(userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/register/requests/{userId}/reject")
    public ResponseEntity<Void> rejectRegistrationApplication(@PathVariable Integer userId) {
        authService.rejectRegistrationRequest(userId);
        return ResponseEntity.noContent().build();
    }
}
