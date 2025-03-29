package com.hseok.vote.dto;

import com.hseok.vote.domain.VoteRecord;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteStatusDto {
    private boolean hasVoted;
    private Long selectedOptionId;
    private String selectedOptionName;

    // 정적 팩토리 메소드
    public static VoteStatusDto from(VoteRecord voteRecord) {
        if (voteRecord == null) {
            return VoteStatusDto.builder()
                    .hasVoted(false)
                    .build();
        }

        return VoteStatusDto.builder()
                .hasVoted(true)
                .selectedOptionId(voteRecord.getVoteOption().getId())
                .selectedOptionName(voteRecord.getVoteOption().getName())
                .build();
    }
}
