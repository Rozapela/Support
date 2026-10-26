package ru.support.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.support.entity.User;
import ru.support.repo.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String login, String rawPassword) {
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Такой логин уже занят");
        }
        User u = new User();
        u.setLogin(login);
        u.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(u);
    }
}