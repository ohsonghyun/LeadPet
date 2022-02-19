package com.leadpet.www.presentation.contorller;

import com.leadpet.www.application.service.UserService;
import com.leadpet.www.presentation.dto.request.SignUpUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody final SignUpUserDto signUpUserDto) {
        // TODO Response로 어떤걸 돌려줄지 결정
        userService.saveNewUser(signUpUserDto.toUsers()); // TODO 어떻게 넘겨줄지 결정
        return ResponseEntity.ok("ok");
    }

}
