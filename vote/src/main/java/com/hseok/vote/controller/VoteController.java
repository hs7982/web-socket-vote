package com.hseok.vote.controller;

import com.hseok.vote.domain.VoteRoom;
import com.hseok.vote.dto.vote.VoteCastRequestDto;
import com.hseok.vote.dto.vote.VoteRoomCreateRequestDto;
import com.hseok.vote.dto.vote.VoteRoomResponseDto;
import com.hseok.vote.service.VoteService;
import com.hseok.vote.user.UserPrincipal;
import com.hseok.vote.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    //get: 투표방 목록
    @GetMapping
    public ResponseEntity<Object> list() {
        List<VoteRoom> voteRoomList = voteService.list();
        return ResponseHandler.responseBuilder(
                HttpStatus.OK,
                "목록 조회 성공",
                voteRoomList
        );
    }
    //get: id의 투표방 상세정보(재목내용 포함 옵션목록, 투표결과까지 포함)
    @GetMapping("/{roomId}")
    public ResponseEntity<Object> getVoteRoomDetail(@PathVariable Long roomId, @AuthenticationPrincipal UserPrincipal principal) {
        VoteRoomResponseDto responseDto = voteService.findById(roomId, principal.getUserId());
        return ResponseHandler.responseBuilder(
                HttpStatus.OK,
                "상세정보 조회 성공.",
                responseDto
        );
    }
    //post: id의 투표방에 투표함
    @PostMapping("/{roomId}/cast")
    public ResponseEntity<Object> castVote(@PathVariable Long roomId,
                                                 @RequestBody VoteCastRequestDto requestDto,
                                                 @AuthenticationPrincipal UserPrincipal principal) {
        voteService.castVote(roomId, requestDto, principal.getUserId());
        return ResponseHandler.responseBuilder(
                HttpStatus.CREATED,
                "투표가 완료되었습니다!",
                null
        );
    }

    @PutMapping("/{roomId}/cast")
    public ResponseEntity<Object> updateVote(@PathVariable Long roomId,
                                           @RequestBody VoteCastRequestDto requestDto,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        voteService.updateVote(roomId, requestDto, principal.getUserId());
        return ResponseHandler.responseBuilder(
                HttpStatus.CREATED,
                "투표가 수정되었습니다!",
                null
        );
    }

    //post: 투표방 생성
    @PostMapping
    public ResponseEntity<Object> createVoteRoom(@RequestBody VoteRoomCreateRequestDto requestDto, @AuthenticationPrincipal UserPrincipal principal) {
        long roomId = voteService.createVoteRoom(requestDto);
        return ResponseHandler.responseBuilder(
                HttpStatus.CREATED,
                "투표방이 생성되었습니다.",
                roomId
        );
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Object> deleteVoteRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserPrincipal principal) {
        voteService.deleteVoteRoom(roomId, principal.getUserId());
        return ResponseHandler.responseBuilder(
                HttpStatus.OK,
                "투표방이 삭제되었습니다.",
                null
        );
    }
}
