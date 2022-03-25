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
                        normalPostService.addNewPost(request.toNormalPost(), request.getUserId())));
    }

    @ApiOperation(value = "일반 게시물 수정")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 게시글"),
            @ApiResponse(code = 403, message = "권한 없는 조작")
    })
    @PutMapping("/update")
    public ResponseEntity<UpdateNormalPostResponseDto> updatePost(@RequestBody UpdateNormalPostRequestDto request) {
        return ResponseEntity.ok(
                UpdateNormalPostResponseDto.from(
                        normalPostService.updateNormalPost(request.toNormalPost(), request.getUserId())));
    }

    @ApiOperation(value = "모든 일반 게시물 취득")
    @GetMapping("/all")
    public ResponseEntity<List<NormalPostResponse>> getAllNormalPosts() {
        return ResponseEntity.ok(NormalPostResponse.from(normalPostService.getAllNormalPosts()));
    }
}

