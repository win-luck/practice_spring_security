package csw.practice.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class UserController {

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh() {
        return ResponseEntity.ok("response");
    }
}
