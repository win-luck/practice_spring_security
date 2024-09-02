package csw.practice.security.auth.component.handler;

import csw.practice.security.auth.component.UserInfo;
import csw.practice.security.auth.component.UserInfoAnnotation;
import csw.practice.security.auth.component.jwt.JwtUtil;
import csw.practice.security.entity.Member;
import csw.practice.security.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInfoAnnotation.class) && parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public UserInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = jwtUtil.getJwtFromHeader(authorizationHeader);
        Long userId = jwtUtil.getUserIdFromAccessToken(jwt);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserInfo(userId, member.getTimeZone());
    }
}
