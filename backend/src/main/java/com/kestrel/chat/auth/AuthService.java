package com.kestrel.chat.auth;

import com.kestrel.chat.auth.dto.LoginRequest;
import com.kestrel.chat.auth.dto.RegisterRequest;
import com.kestrel.chat.user.dto.UserResponse;
import com.kestrel.chat.user.User;
import com.kestrel.chat.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;


@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final String dummyHash;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dummyHash = passwordEncoder.encode("dummy-password-for-timing");
    }

    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {

        String email = normalise(registerRequest.email());
        String username = normalise(registerRequest.username());
        String displayName = registerRequest.displayName().trim();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException();
        }
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException();
        }

        String passwordHash = passwordEncoder.encode(registerRequest.password());
        User user = new User(email, username, displayName, passwordHash);

        try {
            User saved = userRepository.saveAndFlush(user);
            return UserResponse.from(saved);
        } catch (DataIntegrityViolationException ex) {
            throw translateDuplicate(ex);
        }

    }

    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest loginRequest) {
        String email = normalise(loginRequest.email());

        Optional<User> maybeUser = userRepository.findByEmail(email);

        if (maybeUser.isEmpty()) {
            // result ignored on purpose: only here to take as long as a real check
            passwordEncoder.matches(loginRequest.password(), dummyHash);
            throw new InvalidCredentialsException();
        }

        User user = maybeUser.get();

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return UserResponse.from(user);
    }

    private static String normalise(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private static RuntimeException translateDuplicate(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();
        if (message != null && message.contains("uq_users_email")) {
            return new EmailAlreadyTakenException();
        }
        if (message != null && message.contains("uq_users_username")) {
            return new UsernameAlreadyTakenException();
        }
        return ex;
    }

}
