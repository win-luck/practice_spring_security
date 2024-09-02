package csw.practice.security.controller;

import csw.practice.security.dto.JoinRequestDto;
import csw.practice.security.dto.LoginRequestDto;
import csw.practice.security.dto.RequestNewAccessTokenDto;
import csw.practice.security.dto.TokenDto;
import csw.practice.security.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody JoinRequestDto dto) {
        authService.join(dto);
        return ResponseEntity.ok().build();
    }

    // 로그인 (성공 후 JWT 토큰 발급)
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
