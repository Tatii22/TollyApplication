package com.rentaherramientas.tolly.application.usecase.auth;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.RefreshToken;
import com.rentaherramientas.tolly.domain.ports.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Logout
 * Invalida el refresh token
 */
@Service
public class LogoutUseCase {
    
    private final RefreshTokenRepository refreshTokenRepository;
    
    public LogoutUseCase(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    
    @Transactional
    public void execute(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new DomainException("Refresh token no encontrado"));
        
        // Revocar el token
        token.revoke();
        refreshTokenRepository.save(token);
    }
}
