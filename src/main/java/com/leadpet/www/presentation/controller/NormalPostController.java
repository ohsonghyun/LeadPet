package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.NormalPostService;
import com.leadpet.www.presentation.dto.request.post.AddNormalPostRequestDto;
import com.leadpet.www.presentation.dto.request.post.UpdateNormalPostRequestDto;
import com.leadpet.www.presentation.dto.request.post.normal.DeleteNormalPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NormalPostController
 */
@Api(tags = "게시물 컨트롤러")
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
    @ApiResponses({
            @ApiResponse(code = 400, message = "옳지 않은 파라미터")
    })
    @GetMapping("/all")
    public ResponseEntity<ResultWrapper<List<NormalPostResponse>>> getAllNormalPosts(@NonNull @RequestParam final int page, @NonNull @RequestParam final int size) {
        return ResponseEntity.ok(new ResultWrapper<>(NormalPostResponse.from(normalPostService.getNormalPostsWith(page, size))));
    }

    /**
     * Wrapper class
     * <p>배열로 반환되는 타입을 래핑해서 JSON 타입으로 반환하도록 한다</p>
     *
     * @param <T>
     */
    @lombok.Getter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    class ResultWrapper<T> {
        T result;
    }

    @ApiOperation(value = "모든 일반 게시물 카운트")
    @GetMapping("/allCount")
    public ResponseEntity<NormalPostAllCountResponse> getAllCount() {
        Long allCount = normalPostService.getAllNormalPostCount();
        return ResponseEntity.ok(
                NormalPostAllCountResponse.builder()
                        .allCount(allCount)
                        .build());
    }

}
