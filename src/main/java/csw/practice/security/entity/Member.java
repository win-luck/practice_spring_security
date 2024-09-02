package csw.practice.security.entity;

import csw.practice.security.auth.component.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    private String createdAt;

    private UserRoleEnum role;

    public static Member of(String name, String password, String email) {
        Member member = new Member();
        member.name = name;
        member.password = password;
        member.email = email;
        member.createdAt = LocalDateTime.now().toString();
        member.role = UserRoleEnum.ROLE_USER;
        return member;
    }
}
