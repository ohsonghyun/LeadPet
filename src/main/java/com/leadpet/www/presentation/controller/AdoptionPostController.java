package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.AdoptionPostService;
import com.leadpet.www.presentation.dto.request.post.adoption.AddAdoptionPostRequestDto;
import com.leadpet.www.presentation.dto.request.post.donation.AddDonationPostRequestDto;
import com.leadpet.www.presentation.dto.response.post.adoption.AddAdoptionPostResponseDto;
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
    public ResponseEntity<AddAdoptionPostResponseDto> addAdoptionPost(@RequestBody AddAdoptionPostRequestDto newAdoptionPost) {
        return ResponseEntity.ok(
                AddAdoptionPostResponseDto.from(
                        adoptionPostService.addNewPost(newAdoptionPost.toAdoptionPost(), newAdoptionPost.getUserId())));
    }
}
