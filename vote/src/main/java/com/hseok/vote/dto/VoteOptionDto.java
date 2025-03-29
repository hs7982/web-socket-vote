package com.hseok.vote.dto;

import com.hseok.vote.domain.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteOptionDto {
    private Long id;
    private String name;
    private int voteCount;
    private int OptionOrder;

    public static VoteOptionDto from(VoteOption voteOption) {
        return VoteOptionDto.builder()
                .id(voteOption.getId())
                .name(voteOption.getName())
                .voteCount(voteOption.getVoteCount())
                .OptionOrder(voteOption.getOptionOrder())
                .build();
    }
}