package com.roadmaker.global.interceptor;

import com.roadmaker.v1.auth.service.AuthService;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.exception.UnAuthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginRequiredInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(LoginRequired.class)) {

            Optional<Member> member = authService.getLoggedInMember();
            if(member.isEmpty()) {
                throw new UnAuthenticatedException();
            }
        }

        return true;
    }
}
