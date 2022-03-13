package com.leadpet.www.presentation.controller.annotation.config;

import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.exception.WrongArgumentsException;
import com.leadpet.www.presentation.controller.annotation.UserTypes;
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
        return parameter.hasParameterAnnotation(UserTypes.class);
    }

    @Override
    public UserType resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
        String value = servletWebRequest
                .getRequest()
                .getParameter(key);
        if (Objects.isNull(value) || !UserType.has(value)) {
            throw new WrongArgumentsException("Error: 잘못 된 파라미터");
        }
        return UserType.from(value);
    }
}
