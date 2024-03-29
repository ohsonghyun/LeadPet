package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.DonationPostService;
import com.leadpet.www.infrastructure.db.posts.donationPost.condition.SearchDonationPostCondition;
import com.leadpet.www.presentation.dto.request.post.donation.AddDonationPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.donation.AddDonationPostResponseDto;
import com.leadpet.www.presentation.dto.response.post.donation.DonationPostPageResponseDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * DonationPostController
 */
@Api(tags = "기부 게시물 컨트롤러")
@RestController
@RequestMapping(
        value = "/v1/post/donation",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@lombok.RequiredArgsConstructor
public class DonationPostController {

    private final DonationPostService donationPostService;

    @ApiOperation(value = "신규 기부 게시물 추가")
    @ApiResponses({
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PostMapping
    public ResponseEntity<AddDonationPostResponseDto> addDonationPost(
            @RequestBody AddDonationPostRequestDto newDonationPost
    ) {
        return ResponseEntity.ok(
                AddDonationPostResponseDto.from(
                        donationPostService.addNewPost(newDonationPost.toDontaionPost(), newDonationPost.getUserId())));
    }

    @ApiOperation(value = "기부 게시물 취득 (페이지네이션)")
    @GetMapping
    public ResponseEntity<Page<DonationPostPageResponseDto>> searchDonationPosts(
            final SearchDonationPostCondition condition,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(donationPostService.searchAll(condition, pageable));
    }
}
