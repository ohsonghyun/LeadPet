package com.leadpet.www.presentation.contorller;

import com.leadpet.www.application.service.UserService;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.presentation.dto.request.SignUpUserRequestDto;
import com.leadpet.www.presentation.dto.response.SignUpUserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * UserController
 */
@RestController
@RequestMapping("/v1/user")
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpUserResponseDto> signUp(@RequestBody final SignUpUserRequestDto signUpUserRequestDto) {
        Users savedUser = userService.saveNewUser(signUpUserRequestDto.toUsers());
        return ResponseEntity.ok(SignUpUserResponseDto.from(savedUser.getUid()));
    }

}
