package lk.apebodima.api.controller;

import lk.apebodima.api.dto.AuthResponse;
import lk.apebodima.api.dto.LoginRequest;
import lk.apebodima.api.dto.RegisterRequest;
import lk.apebodima.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") // Base URL for all endpoints in this controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; // Inject the service interface


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}