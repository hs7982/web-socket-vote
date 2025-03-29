package com.hseok.vote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vote_option")
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "vote_room_id")
    private VoteRoom voteRoom;

    @Column(nullable = false)
    private String name;

    @Column
    private int OptionOrder;

    @Column
    private int voteCount=0;

    public void increaseVote() {
        this.voteCount++;
    }
}
