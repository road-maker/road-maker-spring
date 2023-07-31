package com.roadmaker.commons.interceptor;

import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.exception.UnAuthenticatedException;
import com.roadmaker.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginRequiredInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            // Request Header에서 JWT 추출
            String token = jwtProvider.resolveToken((HttpServletRequest) request);

            if (token != null && jwtProvider.validationToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 실제 존재하는 Member인지 검사
            memberService.getLoggedInMember().orElseThrow(UnAuthenticatedException::new);
        }

        return true;
    }
}
