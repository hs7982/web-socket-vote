package com.hseok.vote.user;

import com.hseok.vote.user.domain.User;
import com.hseok.vote.user.dto.UserJoinRequest;
import com.hseok.vote.user.dto.UserJoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    public UserJoinResponse userJoin(UserJoinRequest reqDto) {
        validateDuplicateUsername(reqDto.getUsername());
        validateDuplicateEmail(reqDto.getEmail());

        User user = User.builder()
                .username(reqDto.getUsername())
                .email(reqDto.getEmail())
                .password(passwordEncoder.encode(reqDto.getPassword()))
                .enabled(true)

                .build();

        userRepository.save(user);

        return new UserJoinResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    public void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 가입된 계정명 입니다.");
        }
    }

    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }
    }
}
