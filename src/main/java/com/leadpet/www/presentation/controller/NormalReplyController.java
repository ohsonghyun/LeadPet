package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.reply.normal.NormalReplyService;
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import com.leadpet.www.presentation.dto.request.reply.normal.AddNormalReplyRequestDto;
import com.leadpet.www.presentation.dto.response.ErrorResponse;
import com.leadpet.www.presentation.dto.response.reply.normal.AddNormalReplyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NormalReplyController
 */
@Api(tags = "일상피드 댓글 컨트롤러")
@RestController
@RequestMapping("/v1/reply/normal")
@lombok.RequiredArgsConstructor
public class NormalReplyController {
    private final NormalReplyService normalReplyService;

    @ApiOperation(value = "일상피드 댓글 추가")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 404, message = "존재하지 않는 일상피드", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 유저", response = ErrorResponse.class)
    })
    @PostMapping
    public ResponseEntity<AddNormalReplyResponse> addNewNormalReply(
            @RequestBody final AddNormalReplyRequestDto request
    ) {
        NormalReply savedReply = normalReplyService.saveReply(
                request.getNormalPostId(), request.getUserId(), request.getContent());
        return ResponseEntity.ok(AddNormalReplyResponse.from(savedReply));
    }
}
