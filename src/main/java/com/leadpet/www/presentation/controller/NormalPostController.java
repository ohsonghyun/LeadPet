package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.NormalPostService;
import com.leadpet.www.infrastructure.db.posts.normalPost.condition.SearchNormalPostCondition;
import com.leadpet.www.presentation.dto.request.post.AddNormalPostRequestDto;
import com.leadpet.www.presentation.dto.request.post.UpdateNormalPostRequestDto;
import com.leadpet.www.presentation.dto.request.post.normal.DeleteNormalPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.AddNormalPostResponseDto;
import com.leadpet.www.presentation.dto.response.post.DeleteNormalPostResponseDto;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import com.leadpet.www.presentation.dto.response.post.UpdateNormalPostResponseDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * NormalPostController
 */
@Api(tags = "일반 게시물 컨트롤러")
@RestController
@RequestMapping(
        value = "/v1/post/normal",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@lombok.RequiredArgsConstructor
public class NormalPostController {

    private final NormalPostService normalPostService;

    @ApiOperation(value = "신규 일반 게시물 추가")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PostMapping
    public ResponseEntity<AddNormalPostResponseDto> addNewPost(@RequestBody AddNormalPostRequestDto request) {
        return ResponseEntity.ok(
                AddNormalPostResponseDto.from(
                        normalPostService.addNewPost(request.toNormalPost(), request.getUserId())));
    }

    @ApiOperation(value = "일반 게시물 수정")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 게시글"),
            @ApiResponse(code = 403, message = "권한 없는 조작")
    })
    @PutMapping
    public ResponseEntity<UpdateNormalPostResponseDto> updatePost(@RequestBody UpdateNormalPostRequestDto request) {
        return ResponseEntity.ok(
                UpdateNormalPostResponseDto.from(
                        normalPostService.updateNormalPost(request.toNormalPost(), request.getUserId())));
    }

    @ApiOperation(value = "일반 게시물 삭제")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 게시글"),
            @ApiResponse(code = 403, message = "권한 없는 조작")
    })
    @DeleteMapping
    public ResponseEntity<DeleteNormalPostResponseDto> deletePost(@RequestBody DeleteNormalPostRequestDto request) {
        return ResponseEntity.ok(
                DeleteNormalPostResponseDto.from(
                        normalPostService.deleteNormalPost(request.getNormalPostId(), request.getUserId())));
    }

    @ApiOperation(value = "모든 일반 게시물 취득")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저ID"),
            @ApiImplicitParam(name = "likedUserId", value = "'좋아요' 정보가 필요한 유저ID")
    })
    @GetMapping
    public ResponseEntity<Page<NormalPostResponse>> getAllNormalPosts(
            final SearchNormalPostCondition condition,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(normalPostService.getNormalPostsWith(condition, pageable));
    }

}
