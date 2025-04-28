package com.hseok.vote.user.repository;

import com.hseok.vote.user.domain.RefreshToken;
import com.hseok.vote.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findTopByUserOrderByIdDesc(User user);

    void deleteByUser(User user);
}
