package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.SavedPostService;
import com.leadpet.www.presentation.dto.request.user.savedPost.AddSavedPostRequest;
import com.leadpet.www.presentation.dto.response.ErrorResponse;
import com.leadpet.www.presentation.dto.response.reply.normal.AddNormalReplyResponse;
import com.leadpet.www.presentation.dto.response.user.savedPost.AddSavedPostResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SavedPostController
 */
@Api(tags = "저장피드 컨트롤러")
@RestController
@RequestMapping("/v1/savedPost")
@lombok.RequiredArgsConstructor
public class SavedPostController {

    private final SavedPostService savedPostService;

    @ApiOperation(value = "저장피드 추가")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저ID", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 피드ID", response = ErrorResponse.class)
    })
    @PostMapping
    public ResponseEntity<AddSavedPostResponse> addSavedPost(
            @RequestBody final AddSavedPostRequest request
    ) {
        return ResponseEntity.ok(
                AddSavedPostResponse.from(
                        savedPostService.save(request.getPostId(), request.getPostType(), request.getUserId())));
    }

}
