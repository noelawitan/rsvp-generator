package com.nla.rsvp.service;

import com.nla.rsvp.constant.Role;
import com.nla.rsvp.data.RegisterRequest;
import com.nla.rsvp.entity.User;
import com.nla.rsvp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(RegisterRequest registerRequest) {
        User newUser = new User();
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setMiddleName(registerRequest.getMiddleName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setDateOfBirth(registerRequest.getDateOfBirth());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(Role.USER);

        return userRepository.save(newUser);
    }
}