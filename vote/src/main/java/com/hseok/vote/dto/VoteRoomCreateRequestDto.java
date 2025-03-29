package com.hseok.vote.dto;

import com.hseok.vote.domain.VoteRoom;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteRoomCreateRequestDto {
    @NotEmpty(message = "제목은 필수 입력값입니다")
    private String title;

    private String content;

    @NotNull(message = "종료 시간은 필수 입력값입니다")
    private LocalDateTime endTime;

    @NotEmpty(message = "최소 2개 이상의 투표 옵션이 필요합니다")
    private List<String> optionNames;

    public VoteRoom toEntity() {
        return VoteRoom.builder()
                .title(title)
                .content(content)
                .voteCount(0)
                .endTime(endTime)
                .options(new ArrayList<>()) // 빈 리스트로 초기화
                .build();
    }
}
