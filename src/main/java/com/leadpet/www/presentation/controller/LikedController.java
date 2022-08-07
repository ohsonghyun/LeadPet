package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.liked.LikedService;
import com.leadpet.www.infrastructure.domain.liked.LikedResult;
import com.leadpet.www.infrastructure.exception.UnexpectedOperationException;
import com.leadpet.www.presentation.dto.request.liked.UpdateLikedRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LikedController
 */
@Api(tags = "좋아요 컨트롤러")
@Slf4j
@lombok.RequiredArgsConstructor
@RequestMapping("/v1/liked")
@RestController
public class LikedController {

    private final LikedService likedService;

    @ApiOperation("좋아요 상태 갱신")
    @ApiResponses({
            @ApiResponse(code = 201, message = "좋아요 정보 저장"),
            @ApiResponse(code = 200, message = "좋아요 정보 삭제"),
            @ApiResponse(code = 500, message = "좋아요 처리 실패")
    })
    @PostMapping
    public ResponseEntity<Void> updateLiked(@RequestBody final UpdateLikedRequestDto request) {
        LikedResult likedResult = likedService.saveOrDelete(request.getUserId(), request.getPostId());
        switch (likedResult) {
            case CREATED:
                return ResponseEntity.created(null).build();
            case DELETED:
                return ResponseEntity.ok().build();
            default:
                log.error("좋아요 처리 실패\tuserId: {}\tpostId: {}", request.getUserId(), request.getPostId());
                throw new UnexpectedOperationException("Error: 좋아요 처리 실패");
        }
    }
}
