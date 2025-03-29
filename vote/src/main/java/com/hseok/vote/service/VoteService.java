package com.hseok.vote.service;

import com.hseok.vote.domain.VoteOption;
import com.hseok.vote.domain.VoteRecord;
import com.hseok.vote.domain.VoteRoom;
import com.hseok.vote.dto.VoteCastRequestDto;
import com.hseok.vote.dto.VoteRoomCreateRequestDto;
import com.hseok.vote.dto.VoteRoomResponseDto;
import com.hseok.vote.repository.VoteOptionRepository;
import com.hseok.vote.repository.VoteRecordRepository;
import com.hseok.vote.repository.VoteRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRoomRepository voteRoomRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;

    public List<VoteRoom> list() {
        return voteRoomRepository.findAll();
    }

    public VoteRoomResponseDto findById(long id, long userId) {
        VoteRoom voteRoom = voteRoomRepository.findById(id).orElseThrow(()->new EntityNotFoundException("id: "+id+"에 해당되는 투표를 찾을 수 없습니다!"));
        VoteRecord userVoteRecord = voteRecordRepository.findByUserIdAndVoteRoom(userId, voteRoom).orElse(null);

        return VoteRoomResponseDto.from(voteRoom, userVoteRecord);
    }

    @Transactional
    public Long createVoteRoom(VoteRoomCreateRequestDto requestDto) {
        // 1. VoteRoom 엔티티 생성 및 저장
        VoteRoom voteRoom = requestDto.toEntity();
        voteRoomRepository.save(voteRoom);

        // 2. 옵션들 생성 및 저장
        List<VoteOption> options = new ArrayList<>();
        int optionOrder = 1;
        for (String optionName : requestDto.getOptionNames()) {
            VoteOption option = VoteOption.builder()
                    .name(optionName)
                    .voteRoom(voteRoom)
                    .OptionOrder(optionOrder++)
                    .build();
            options.add(option);
            voteOptionRepository.save(option);
        }

        // 3. 옵션들 설정 (양방향 관계 설정)
        voteRoom.getOptions().addAll(options);

        return voteRoom.getId();
    }

    @Transactional
    public void castVote(Long roomId, VoteCastRequestDto requestDto, long userId) {
        //id를 기반으로 투표방 정보를 가져옴
        VoteRoom voteRoom = voteRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException("투표방을 찾을 수 없습니다."));
        //id를 기반으로 옵션 정보를 가져옴
        VoteOption voteOption = voteOptionRepository.findById(requestDto.getOptionId()).orElseThrow(()->new EntityNotFoundException("투표 ID를 찾을 수 없습니다."));
        //해당 투표방에 이미 투표를 했는지 검증
        if (voteRecordRepository.existsByUserIdAndVoteRoom(userId, voteRoom))
            throw new IllegalArgumentException("이미 해당 투표에 참여하였습니다!");

        //1. 투표 기록 엔티티를 생성하여 저장
        VoteRecord voteRecord = VoteRecord.builder()
                .userId(userId)
                .voteRoom(voteRoom)
                .voteOption(voteOption)
                .build();
        voteRecordRepository.save(voteRecord);

        //2. 투표방의 총 투표수를 증가시킴
        voteRoom.increaseVote();
        voteRoomRepository.save(voteRoom);

        //3. 투표 옵션의 투표수를 증가시킴
        voteOption.increaseVote();
        voteOptionRepository.save(voteOption);
    }
}
