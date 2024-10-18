package com.woopaca.jissisplit.security.auth.service;

import com.woopaca.jissisplit.common.error.exception.InvalidRefreshTokenException;
import com.woopaca.jissisplit.security.auth.model.Tokens;
import com.woopaca.jissisplit.security.token.JwtProvider;
import com.woopaca.jissisplit.security.token.RefreshTokenProvider;
import com.woopaca.jissisplit.user.User;
import com.woopaca.jissisplit.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final UserRepository userRepository;

    public AuthService(JwtProvider jwtProvider, RefreshTokenProvider refreshTokenProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userRepository = userRepository;
    }

    public Tokens issueTokensFor(User user) {
        refreshTokenProvider.expireRefreshToken(user);
        String accessToken = jwtProvider.issueAccessToken(user);
        String refreshToken = refreshTokenProvider.issueRefreshToken(user);
        return new Tokens(accessToken, refreshToken);
    }

    public Tokens reissueTokensBy(String refreshToken) {
        String subject = refreshTokenProvider.takeOutSubject(refreshToken);
        User principal = userRepository.findByEmail(subject)
                .orElseThrow(InvalidRefreshTokenException::new);
        String reissuedAccessToken = jwtProvider.issueAccessToken(principal);
        String reissuedRefreshToken = refreshTokenProvider.issueRefreshToken(principal);
        return new Tokens(reissuedAccessToken, reissuedRefreshToken);
    }
}
