package com.hseok.vote.dto;

import com.hseok.vote.domain.VoteRecord;
import com.hseok.vote.domain.VoteRoom;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteRoomResponseDto {
    private Long id;
    private String title;
    private String content;
    private Integer voteCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime endTime;
    private List<VoteOptionDto> options;
    private VoteStatusDto userVoteStatus; // 사용자의 투표 상태

    public static VoteRoomResponseDto from(VoteRoom voteRoom, VoteRecord userVoteRecord) {
        return VoteRoomResponseDto.builder()
                .id(voteRoom.getId())
                .title(voteRoom.getTitle())
                .content(voteRoom.getContent())
                .voteCount(voteRoom.getVoteCount())
                .createTime(voteRoom.getCreateTime())
                .updateTime(voteRoom.getUpdateTime())
                .endTime(voteRoom.getEndTime())
                .options(voteRoom.getOptions().stream()
                        .map(VoteOptionDto::from)
                        .collect(Collectors.toList()))
                .userVoteStatus(VoteStatusDto.from(userVoteRecord))
                .build();
    }
}
