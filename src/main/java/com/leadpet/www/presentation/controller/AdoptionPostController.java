package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.AdoptionPostService;
import com.leadpet.www.infrastructure.db.adoptionPost.condition.SearchAdoptionPostCondition;
import com.leadpet.www.presentation.dto.request.post.adoption.AddAdoptionPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.adoption.AddAdoptionPostResponseDto;
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AdoptionPostController
 */
@Api(tags = "입양 게시물 컨트롤러")
@RestController
@RequestMapping(
        value = "/v1/post/adoption",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@lombok.RequiredArgsConstructor
public class AdoptionPostController {

    private final AdoptionPostService adoptionPostService;

    @ApiOperation(value = "신규 입양 게시물 추가")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PostMapping
    public ResponseEntity<AddAdoptionPostResponseDto> addAdoptionPost(@RequestBody final AddAdoptionPostRequestDto newAdoptionPost) {
        return ResponseEntity.ok(
                AddAdoptionPostResponseDto.from(
                        adoptionPostService.addNewPost(newAdoptionPost.toAdoptionPost(), newAdoptionPost.getUserId())));
    }

    @ApiOperation(value = "입양 게시물 취득 (페이지네이션)")
    @GetMapping
    public ResponseEntity<Page<AdoptionPostPageResponseDto>> searchAdoptionPosts(
            final SearchAdoptionPostCondition condition,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(adoptionPostService.searchAll(condition, pageable));
    }
}
