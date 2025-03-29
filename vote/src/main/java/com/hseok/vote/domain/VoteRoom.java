package com.hseok.vote.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vote_room")
public class VoteRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Integer voteCount;

    @CreationTimestamp
    @Column
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column
    private LocalDateTime updateTime;

    @Column
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "voteRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteOption> options;

    public void increaseVote() {
        this.voteCount++;
    }
}
