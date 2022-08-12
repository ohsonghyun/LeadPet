package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.SavedPostService;
import com.leadpet.www.presentation.dto.request.user.savedPost.AddSavedPostRequest;
import com.leadpet.www.presentation.dto.request.user.savedPost.DeleteSavedPostRequest;
import com.leadpet.www.presentation.dto.response.ErrorResponse;
import com.leadpet.www.presentation.dto.response.post.adoption.SimpleAdoptionPostResponse;
import com.leadpet.www.presentation.dto.response.post.donation.SimpleDonationPostResponse;
import com.leadpet.www.presentation.dto.response.post.normal.SimpleNormalPostResponse;
import com.leadpet.www.presentation.dto.response.reply.normal.AddNormalReplyResponse;
import com.leadpet.www.presentation.dto.response.user.savedPost.AddSavedPostResponse;
import com.leadpet.www.presentation.dto.response.user.savedPost.DeleteSavedPostResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "저장피드 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저ID", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "권한 없는 유저", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 피드ID", response = ErrorResponse.class)
    })
    @DeleteMapping
    public ResponseEntity<DeleteSavedPostResponse> deleteSavedPost(
            @RequestBody final DeleteSavedPostRequest request
    ) {
        return ResponseEntity.ok(
                DeleteSavedPostResponse.from(
                        savedPostService.deleteById(request.getUserId(), request.getSavedPostId())));
    }

    @ApiOperation(value = "저장피드 취득(일상피드)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상")
    })
    @GetMapping("/normalPost/{userId}")
    public ResponseEntity<Page<SimpleNormalPostResponse>> getSavedNormalPostsByUserId(
            @PathVariable final String userid,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(savedPostService.findNormalPostByUserId(userid, pageable));
    }

    @ApiOperation(value = "저장피드 취득(입양피드)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상")
    })
    @GetMapping("/adoptionPost/{userId}")
    public ResponseEntity<Page<SimpleAdoptionPostResponse>> getSavedAdoptionPostsByUserId(
            @PathVariable final String userId,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(savedPostService.findAdoptionPostByUserId(userId, pageable));
    }

    @ApiOperation(value = "저장피드 취득(기부피드)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상")
    })
    @GetMapping("/donationPost/{userId}")
    public ResponseEntity<Page<SimpleDonationPostResponse>> getSavedDonationPostsByUserId(
            @PathVariable final String userId,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(savedPostService.findDonationPostByUserId(userId, pageable));
    }
}
