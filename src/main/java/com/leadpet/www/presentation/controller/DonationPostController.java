package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.DonationPostService;
import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import com.leadpet.www.presentation.dto.request.post.donation.AddDonationPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.donation.AddDonationPostResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AddDonationPostResponseDto> addDonationPost(@RequestBody AddDonationPostRequestDto newDonationPost) {
        return ResponseEntity.ok(
                AddDonationPostResponseDto.from(
                        donationPostService.addNewPost(newDonationPost.toDontaionPost(), newDonationPost.getUserId())));
    }
}
