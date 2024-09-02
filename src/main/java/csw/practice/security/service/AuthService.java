package csw.practice.security.service;

import csw.practice.security.auth.component.jwt.JwtUtil;
import csw.practice.security.dto.*;
import csw.practice.security.entity.Member;
import csw.practice.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void join(JoinRequestDto dto) {
        String name = dto.getName();
        String email = dto.getEmail();
        String password = dto.getPassword();

        if(memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        memberRepository.save(Member.of(name, passwordEncoder.encode(password), email));
    }

    @Transactional(readOnly = true)
    public TokenDto login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Long userId = member.getId();
        String accessToken = jwtUtil.createAccessToken(userId, List.of(member.getRole().toString()));
        String refreshToken = jwtUtil.createRefreshToken(userId, List.of(member.getRole().toString()));
        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public ResponseMemberDto getMyInfo(Long userId) {
        log.info("userId: {}", userId);
        return memberRepository.findById(userId)
                .map(ResponseMemberDto::from)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }
}
