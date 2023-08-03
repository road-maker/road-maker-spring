package com.roadmaker.commons.interceptor;

import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.exception.UnAuthenticatedException;
import com.roadmaker.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginRequiredInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            // 실제 존재하는 Member인지 검사
            memberService.getLoggedInMember().orElseThrow(UnAuthenticatedException::new);
        }

        return true;
    }
}
