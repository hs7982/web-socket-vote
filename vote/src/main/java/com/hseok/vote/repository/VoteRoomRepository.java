package com.hseok.vote.repository;

import com.hseok.vote.domain.VoteRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRoomRepository extends JpaRepository<VoteRoom, Long> {

}
