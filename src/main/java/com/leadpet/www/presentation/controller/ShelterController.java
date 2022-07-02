package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.UserService;
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "보호소 컨트롤러")
@RestController
@RequestMapping("/v1/shelter")
@lombok.RequiredArgsConstructor
public class ShelterController {

    private final UserService userService;

    @ApiOperation(value = "보호소 목록 취득")
    @GetMapping("/list")
    public ResponseEntity<Page<ShelterPageResponseDto>> getListWithCondition(SearchShelterCondition condition, Pageable pageable) {
        return ResponseEntity.ok(userService.searchShelters(condition, pageable));
    }

}
