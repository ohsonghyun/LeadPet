package com.leadpet.www.presentation.controller;

import com.leadpet.www.application.service.UserService;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException;
import com.leadpet.www.presentation.controller.annotation.UserType;
import com.leadpet.www.presentation.dto.request.LogInRequestDto;
import com.leadpet.www.presentation.dto.request.SignUpUserRequestDto;
import com.leadpet.www.presentation.dto.response.LogInResponseDto;
import com.leadpet.www.presentation.dto.response.SignUpUserResponseDto;
import com.leadpet.www.presentation.dto.response.UserListResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    public ResponseEntity<List<UserListResponseDto>> listBy(@UserType final Users.UserType ut) {
        return ResponseEntity.ok(UserListResponseDto.from(userService.getUserListBy(ut)));
    }

}
