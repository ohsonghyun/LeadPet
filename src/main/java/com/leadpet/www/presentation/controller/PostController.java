package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.NormalPostService;
import com.leadpet.www.presentation.dto.response.NormalPostResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/allNormal")
    public ResponseEntity<List<NormalPostResponse>> getAllNormalPosts() {
        return ResponseEntity.ok(NormalPostResponse.from(normalPostService.getAllNormalPosts()));
    }

}

