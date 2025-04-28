package com.hseok.vote.user;

import com.hseok.vote.repository.RoleRepository;
import com.hseok.vote.user.domain.Role;
import com.hseok.vote.user.domain.User;
import com.hseok.vote.user.dto.UserJoinRequest;
import com.hseok.vote.user.dto.UserJoinResponse;
import com.hseok.vote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserJoinResponse userJoin(UserJoinRequest reqDto) {
        validateDuplicateUsername(reqDto.getUsername());
        validateDuplicateEmail(reqDto.getEmail());

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("기본 역할을 찾을 수 없습니다."));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .username(reqDto.getUsername())
                .email(reqDto.getEmail())
                .password(passwordEncoder.encode(reqDto.getPassword()))
                .enabled(true)
                .roles(roles)
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
