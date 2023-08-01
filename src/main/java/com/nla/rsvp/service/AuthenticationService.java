package com.nla.rsvp.service;

import com.nla.rsvp.data.AuthenticationRequest;
import com.nla.rsvp.data.AuthenticationResponse;
import com.nla.rsvp.data.RegisterRequest;
import com.nla.rsvp.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final UserService userService;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse createAuthenticationResponse(User user) {
        final String token = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(token, refreshToken);
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.createToken(user, token);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.createToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
