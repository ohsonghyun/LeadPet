package com.leadpet.www.presentation.controller.annotation.config;

import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.presentation.controller.annotation.UserType;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * UserTypeHandlerConfig
 */
@lombok.RequiredArgsConstructor
public class UserTypeArgumentResolver implements HandlerMethodArgumentResolver {

    private final String key = "ut";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserType.class);
    }

    @Override
    public Users.UserType resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
        String value = servletWebRequest
                .getRequest()
                .getParameter(key);
        if (Objects.isNull(value)) {
            // TODO 예외 커즈터마이즈
            throw new IllegalArgumentException();
        }
        return Users.UserType.from(value);
    }
}
