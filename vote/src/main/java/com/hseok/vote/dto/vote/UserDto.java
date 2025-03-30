package com.hseok.vote.dto.vote;

import com.hseok.vote.domain.VoteRecord;
import com.hseok.vote.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String username;

    public static UserDto from(VoteRecord voteRecord) {
        return UserDto.builder()
                .userId(voteRecord.getUser().getId())
                .username(voteRecord.getUser().getUsername())
                .build();

    }
}
