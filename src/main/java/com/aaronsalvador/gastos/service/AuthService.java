package com.aaronsalvador.gastos.service;

import com.aaronsalvador.gastos.dto.AuthResponse;
import com.aaronsalvador.gastos.dto.LoginRequest;
import com.aaronsalvador.gastos.dto.RegisterRequest;
import com.aaronsalvador.gastos.exception.EmailAlreadyUsedException;
import com.aaronsalvador.gastos.model.User;
import com.aaronsalvador.gastos.repository.UserRepository;
import com.aaronsalvador.gastos.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }
        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return AuthResponse.bearer(jwtService.generateToken(email));
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        return AuthResponse.bearer(jwtService.generateToken(email));
    }
}
