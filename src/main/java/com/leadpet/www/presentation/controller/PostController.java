package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.NormalPostService;
import com.leadpet.www.presentation.dto.request.AddNormalPostRequestDto;
import com.leadpet.www.presentation.dto.response.AddNormalPostResponseDto;
import com.leadpet.www.presentation.dto.response.NormalPostResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PostController
 */
@Api(tags = "게시물 컨트롤러")
@RestController
@RequestMapping("/v1/post")
@lombok.RequiredArgsConstructor
public class PostController {

    private final NormalPostService normalPostService;

    @ApiOperation(value = "신규 일반 게시물 추가")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @PostMapping("/addNormal")
    public ResponseEntity<AddNormalPostResponseDto> addNewPost(@RequestBody AddNormalPostRequestDto request) {
        return ResponseEntity.ok(
                AddNormalPostResponseDto.from(
                        normalPostService.addNewPost(request.toNormalPost(), request.getUid(), request.getLoginMethod())));
    }

    @ApiOperation(value = "모든 일반 게시물 취득")
    @GetMapping("/allNormal")
    public ResponseEntity<List<NormalPostResponse>> getAllNormalPosts() {
        return ResponseEntity.ok(NormalPostResponse.from(normalPostService.getAllNormalPosts()));
    }

}

