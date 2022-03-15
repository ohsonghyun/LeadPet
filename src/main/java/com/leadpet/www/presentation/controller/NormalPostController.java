package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.NormalPostService;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.presentation.dto.request.post.AddNormalPostRequestDto;
import com.leadpet.www.presentation.dto.request.post.UpdateNormalPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.AddNormalPostResponseDto;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import com.leadpet.www.presentation.dto.response.post.UpdateNormalPostResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NormalPostController
 */
@Api(tags = "게시물 컨트롤러")
@RestController
@RequestMapping("/v1/post/normal")
@lombok.RequiredArgsConstructor
public class NormalPostController {

    private final NormalPostService normalPostService;

    @ApiOperation(value = "신규 일반 게시물 추가")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PostMapping("/add")
    public ResponseEntity<AddNormalPostResponseDto> addNewPost(@RequestBody AddNormalPostRequestDto request) {
        return ResponseEntity.ok(
                AddNormalPostResponseDto.from(
                        normalPostService.addNewPost(request.toNormalPost(), request.getUid(), request.getLoginMethod())));
    }

    @ApiOperation(value = "일반 게시물 수정")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 게시글")
    })
    @PutMapping("/update")
    public ResponseEntity<UpdateNormalPostResponseDto> updatePost(@RequestBody UpdateNormalPostRequestDto request) {
        // TODO DTO 수정해서 userId와 postId 조합으로 특정 포스트 업데이트 가능하도록 변경할 것.
        // 삭제하려고하는 postId
        // 삭제를 실행한 userId

        // TODO update & delete 같은 경우는 유저판단을 위해 userId, uid, loginMethod를 받을 필요가 있다. 수정 필요.
        NormalPosts updatedNormalPost = normalPostService.updateNormalPost(request.toNormalPost(), request.getLoginMethod(), request.getUid());
        return ResponseEntity.ok(UpdateNormalPostResponseDto.from(updatedNormalPost, request.getLoginMethod(), request.getUid()));
    }

    @ApiOperation(value = "모든 일반 게시물 취득")
    @GetMapping("/all")
    public ResponseEntity<List<NormalPostResponse>> getAllNormalPosts() {
        return ResponseEntity.ok(NormalPostResponse.from(normalPostService.getAllNormalPosts()));
    }
}

