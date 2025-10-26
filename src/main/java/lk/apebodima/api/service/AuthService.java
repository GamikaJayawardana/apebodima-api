package lk.apebodima.api.service;

import lk.apebodima.api.dto.AuthResponse;
import lk.apebodima.api.dto.LoginRequest;
import lk.apebodima.api.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}