package csw.practice.security.auth.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String timeZone;
}
