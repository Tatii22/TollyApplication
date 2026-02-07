package com.rentaherramientas.tolly.application.usecase;

import com.rentaherramientas.tolly.application.dto.LoginRequest;
import com.rentaherramientas.tolly.application.dto.LoginResponse;
import com.rentaherramientas.tolly.domain.exceptions.InvalidCredentialsException;
import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.model.RefreshToken;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.PasswordService;
import com.rentaherramientas.tolly.domain.ports.RefreshTokenRepository;
import com.rentaherramientas.tolly.domain.ports.TokenService;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Caso de uso: Login
 * Genera JWT access token y refresh token
 */
@Service
public class LoginUseCase {
    
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Long accessTokenExpiration;
    
    public LoginUseCase(
            UserRepository userRepository,
            PasswordService passwordService,
            TokenService tokenService,
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.access-token-expiration}") Long accessTokenExpiration) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenExpiration = accessTokenExpiration;
    }
    
    @Transactional
    public LoginResponse execute(LoginRequest request) {
        // Buscar usuario
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("Usuario con email " + request.email() + " no encontrado"));
        
        // Verificar si está activo
        if (!user.isActive()) {
            throw new InvalidCredentialsException("Usuario bloqueado o inactivo");
        }
        
        // Verificar contraseña
        if (!passwordService.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        
        // Generar access token
        String accessToken = tokenService.generateAccessToken(
            user.getId(),
            user.getEmail(),
            user.getRoles()
        );
        
        // Generar refresh token
        String refreshTokenString = tokenService.generateRefreshToken();
        
        // Calcular expiración del refresh token (7 días por defecto)
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        
        // Crear y guardar refresh token
        RefreshToken refreshToken = RefreshToken.create(
            user.getId(),
            refreshTokenString,
            expiresAt
        );
        refreshTokenRepository.save(refreshToken);
        
        return LoginResponse.of(accessToken, refreshTokenString, accessTokenExpiration / 1000);
    }
}
