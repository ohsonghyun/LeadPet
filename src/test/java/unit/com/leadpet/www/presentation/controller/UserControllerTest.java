package com.leadpet.www.presentation.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.presentation.dto.request.SignUpUserRequestDto;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private final String USER_URL = "/v1/user";

    @LocalServerPort
    private int port;

    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void user_signUp_success() throws Exception {
        // given
        var signUpUserRequestDto = SignUpUserRequestDto.builder()
                .loginMethod(Users.LoginMethod.KAKAO)
                .uid("kakaoUid")
                .name("kakao")
                .build();

        // expect
        mvc.perform(post("http://localhost:" + port + USER_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpUserRequestDto)))
                .andExpect(status().is2xxSuccessful());
    }
}
