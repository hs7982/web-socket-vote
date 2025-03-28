package com.hseok.vote.service;

import com.hseok.vote.domain.VoteRecord;
import com.hseok.vote.domain.VoteRoom;
import com.hseok.vote.repository.VoteRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    VoteRoomRepository voteRoomRepository;
    public List<VoteRoom> list() {
        return voteRoomRepository.findAll();
    }
}
