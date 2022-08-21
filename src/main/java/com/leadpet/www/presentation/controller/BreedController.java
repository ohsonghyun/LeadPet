package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.UserService;
import com.leadpet.www.application.service.breed.BreedService;
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.presentation.dto.request.breed.AddBreedRequest;
import com.leadpet.www.presentation.dto.response.breed.AddBreedResponse;
import com.leadpet.www.presentation.dto.response.breed.AllCountBreedResponse;
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResponse;
import com.leadpet.www.presentation.dto.response.shelter.ShelterDetailDto;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "품종 컨트롤러")
@RestController
@RequestMapping("/v1/breed")
@lombok.RequiredArgsConstructor
public class BreedController {
    private final BreedService breedService;

    @ApiOperation("품종 추가")
    @PostMapping
    public ResponseEntity<AddBreedResponse> addBreed(@RequestBody AddBreedRequest request) {
        return ResponseEntity.ok(
                AddBreedResponse.from(breedService.save(request.getCategory(), request.getBreedName())));
    }

    @ApiOperation("품종 리스트 취득")
    @GetMapping
    public ResponseEntity<Map<String, List<SearchBreedResponse>>> findGroupByCategory() {
        return ResponseEntity.ok(breedService.findGroupByCategory());
    }

    @ApiOperation("품종 전체 카운트 취득")
    @GetMapping("/count")
    public ResponseEntity<AllCountBreedResponse> allCountBreed() {
        return ResponseEntity.ok(
                AllCountBreedResponse.builder()
                        .count(breedService.count())
                        .build());
    }
}
