package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.UserService;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException;
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException;
import com.leadpet.www.presentation.controller.annotation.UserTypes;
import com.leadpet.www.presentation.dto.request.user.LogInRequestDto;
import com.leadpet.www.presentation.dto.request.user.SignUpUserRequestDto;
import com.leadpet.www.presentation.dto.response.user.LogInResponseDto;
import com.leadpet.www.presentation.dto.response.user.SignUpUserResponseDto;
import com.leadpet.www.presentation.dto.response.user.UserDetailResponseDto;
import com.leadpet.www.presentation.dto.response.user.UserListResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * UserController
 */
@Api(tags = "유저 컨트롤러")
@RestController
@RequestMapping("/v1/user")
@Slf4j
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원가입 성공"),
            @ApiResponse(code = 400, message = "필수 데이터 누락 에러"),
            @ApiResponse(code = 409, message = "이미 존재하는 유저 에러")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignUpUserResponseDto> signUp(@Valid @RequestBody final SignUpUserRequestDto signUpUserRequestDto) {
        Users savedUser = userService.saveNewUser(signUpUserRequestDto.toUsers());
        return ResponseEntity.ok(SignUpUserResponseDto.from(savedUser.getUid()));
    }

    @ApiOperation(value = "로그인", notes = "로그인 유형이 Email인 경우 email, password 필수")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "필수 데이터 누락 에러"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저 에러")
    })
    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> logIn(@Valid @RequestBody final LogInRequestDto logInRequestDto) {
        if (!logInRequestDto.hasAllRequiredValue()) {
            log.info("로그인 필수 데이터 입력 누락");
            throw new UnsatisfiedRequirementException("Error: 필수 데이터 입력 누락");
        }
        return ResponseEntity.ok(LogInResponseDto.from(userService.logIn(logInRequestDto.toUsers())));
    }

    @ApiOperation(value = "관리자 로그인", notes = "관리자 로그인은 EMAIL 로그인만 가능 / email, password 필수")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "필수 데이터 누락 에러"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저 에러")
    })
    @PostMapping("/adminLogin")
    public ResponseEntity<LogInResponseDto> logInAdmin(@Valid @RequestBody final LogInRequestDto logInRequestDto) {
        if (!logInRequestDto.checkAdmin()) {
            log.info("관리자 정보 없음");
            throw new UnauthorizedUserException("Error: 권한 없는 접근");
        }
        if (!logInRequestDto.hasAllRequiredValue()) {
            log.info("로그인 필수 데이터 입력 누락");
            throw new UnsatisfiedRequirementException("Error: 필수 데이터 입력 누락");
        }
        return ResponseEntity.ok(LogInResponseDto.from(userService.logIn(logInRequestDto.toUsers())));
    }

    @ApiOperation(value = "유저리스트 취득")
    @ApiResponses({
            @ApiResponse(code = 200, message = "유저리스트 취득 성공"),
            @ApiResponse(code = 400, message = "필수 데이터 누락 에러")
    })
    @GetMapping("/list")
    public ResponseEntity<List<UserListResponseDto>> listBy(@UserTypes final UserType ut) {
        return ResponseEntity.ok(UserListResponseDto.from(userService.getUserListBy(ut)));
    }

    @ApiOperation(value = "유저 디테일 취득")
    @ApiResponses({
            @ApiResponse(code = 200, message = "유저 디테일 취득 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
            @ApiResponse(code = 400, message = "필수 테이터 부족")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponseDto> getUserDetail(@PathVariable final String userId) {
        return ResponseEntity.ok(userService.normalUserDetail(userId));
    }

}
