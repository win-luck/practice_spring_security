package csw.practice.security.controller;

import csw.practice.security.auth.component.IgnoreAuthorize;
import csw.practice.security.auth.component.UserInfo;
import csw.practice.security.auth.component.UserInfoAnnotation;
import csw.practice.security.dto.JoinRequestDto;
import csw.practice.security.dto.LoginRequestDto;
import csw.practice.security.dto.RequestNewAccessTokenDto;
import csw.practice.security.dto.TokenDto;
import csw.practice.security.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @IgnoreAuthorize
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody JoinRequestDto dto) {
        authService.join(dto);
        return ResponseEntity.ok().build();
    }


    // 로그인 (성공 후 JWT 토큰 발급)
    @IgnoreAuthorize
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> example(@UserInfoAnnotation UserInfo userInfo) {
        log.info("User ID: {}", userInfo.getUserId());
        log.info("Time Zone: {}", userInfo.getTimeZone());

        return ResponseEntity.ok("Success");
    }
}
