package com.hseok.vote.dto.vote;

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
