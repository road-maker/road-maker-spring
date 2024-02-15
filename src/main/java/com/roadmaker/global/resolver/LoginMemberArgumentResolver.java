package com.roadmaker.global.resolver;

import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.exception.UnAuthenticatedException;
import com.roadmaker.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return memberService.getLoggedInMember().orElseThrow(UnAuthenticatedException::new);
    }
}
