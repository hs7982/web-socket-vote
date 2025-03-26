package com.hseok.vote.controller;

import com.hseok.vote.domain.VoteRoom;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {
    //get: 투표방 목록
    @GetMapping
    public ResponseEntity<List<VoteRoom>> list() {
        return null;
    }

    //get: id의 투표방 상세정보(재목내용 포함 옵션목록, 투표결과까지 포함)
    @GetMapping("/{roomId}")
    public ResponseEntity<VoteRoom> getVoteRoomDetail(@PathVariable Long roomId) {
        return null;
    }
    //post: id의 투표방에 투표함
    @PostMapping("/{roomId}/vote")
    public ResponseEntity<Void> castVote(@PathVariable Long roomId) {
        return null;
    }

    //post: 투표방 생성
    @PostMapping
    public ResponseEntity<VoteRoom> createVoteRoom(@RequestBody VoteRoom voteRoom) {
        return null;
    }
}
