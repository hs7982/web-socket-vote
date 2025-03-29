package com.hseok.vote.dto;

import com.hseok.vote.domain.VoteOption;
import com.hseok.vote.domain.VoteRecord;
import com.hseok.vote.domain.VoteRoom;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteCastRequestDto {
    @NotNull(message = "투표 옵션을 선택해야 합니다")
    private Long optionId;
}
