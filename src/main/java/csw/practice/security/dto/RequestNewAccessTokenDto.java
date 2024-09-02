package csw.practice.security.dto;

import lombok.Getter;

@Getter
public class RequestNewAccessTokenDto {

    private String refreshToken;

    public RequestNewAccessTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
