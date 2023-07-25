package com.roadmaker.commons.interceptor;

import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.exception.UnAuthenticatedException;
import com.roadmaker.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class LoginRequiredInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LOGGGGGGG");
        System.out.println(handler instanceof HandlerMethod);
        System.out.println(((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class));

        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            // Request Header에서 JWT 추출
            String token = resolveToken((HttpServletRequest) request);

            System.out.println("token = " + token);

            System.out.println("jwtProvider.validationToken(token) = " + jwtProvider.validationToken(token));

            if (token != null && jwtProvider.validationToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                System.out.println("authentication = " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 실제 존재하는 Member인지 검

            Member member = memberService.getLoggedInMember();

            if(member == null) {
                throw new UnAuthenticatedException();
            }
        }

        return true;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }}
