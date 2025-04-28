package com.hseok.vote.dto.vote;

import com.hseok.vote.domain.VoteOption;
import com.hseok.vote.domain.VoteRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteOptionDto {
    private Long id;
    private String name;
    private int voteCount;
    private int OptionOrder;
    private List<UserDto> voters;

    public static VoteOptionDto from(VoteOption voteOption, List<VoteRecord> allVoters) {
        List<UserDto> voters = allVoters.stream()
                .filter(record -> record.getVoteOption().getId().equals(voteOption.getId()))
                .map(UserDto::from)
                .collect(Collectors.toList());

        return VoteOptionDto.builder()
                .id(voteOption.getId())
                .name(voteOption.getName())
                .voteCount(voteOption.getVoteCount())
                .OptionOrder(voteOption.getOptionOrder())
                .voters(voters)
                .build();
    }
}