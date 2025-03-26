package com.hseok.vote.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "vote_record", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "vote_room_id"})})
public class VoteRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_room_id", nullable = false)
    private VoteRoom voteRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_option_id", nullable = false)
    private VoteOption voteOption; // 선택한 옵션 FK

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt; // 투표한 시간
}
