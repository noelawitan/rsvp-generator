package com.nla.rsvp.service;

import com.nla.rsvp.entity.Token;
import com.nla.rsvp.entity.User;
import com.nla.rsvp.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public void createToken(User user, String tokenStr) {
        Token token = new Token();
        token.setTokenValue(tokenStr);
        token.setExpired(false);
        token.setRevokeToken(false);
        token.setExpirationDate(LocalDateTime.now());
        token.setUser(user);
        tokenRepository.save(token);

    }

    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevokeToken(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
