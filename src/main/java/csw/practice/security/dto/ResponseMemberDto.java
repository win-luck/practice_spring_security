package csw.practice.security.dto;

import csw.practice.security.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMemberDto {

    private String name;
    private String email;
    private String createdAt;

    public static ResponseMemberDto from(Member member) {
        return new ResponseMemberDto(member.getName(), member.getEmail(), member.getCreatedAt());
    }
}
