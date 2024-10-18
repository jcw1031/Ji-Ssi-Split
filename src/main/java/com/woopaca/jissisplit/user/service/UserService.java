package com.woopaca.jissisplit.user.service;

import com.woopaca.jissisplit.security.auth.oauth2.KakaoUser;
import com.woopaca.jissisplit.user.User;
import com.woopaca.jissisplit.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerOAuth2User(KakaoUser kakaoUser) {
        return userRepository.findByEmail(kakaoUser.email())
                .orElseGet(() -> {
                    User newUser = User.fromOAuth2User(kakaoUser);
                    return userRepository.save(newUser);
                });
    }
}
