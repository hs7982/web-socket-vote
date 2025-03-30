package com.hseok.vote.repository;

import com.hseok.vote.domain.VoteRecord;
import com.hseok.vote.domain.VoteRoom;
import com.hseok.vote.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRecordRepository extends JpaRepository<VoteRecord, Long> {
    // userId와 voteRoomId로 투표 기록 존재 여부 확인
    boolean existsByUserIdAndVoteRoom(Long userId, VoteRoom voteRoom);

    // userId와 voteRoomId로 투표 기록 조회
    Optional<VoteRecord> findByUserAndVoteRoom(User user, VoteRoom voteRoom);
    Optional<VoteRecord> findByUserIdAndVoteRoom(long userId, VoteRoom voteRoom);

    boolean existsByUserAndVoteRoom(User user, VoteRoom voteRoom);

    Optional<List<VoteRecord>> findByVoteRoom(VoteRoom voteRoom);
}
