package csw.practice.security.auth.component.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    private final HttpRequestEndpointChecker endpointChecker;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (!endpointChecker.isEndpointExist(request)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
        } else {
            super.commence(request, response, authException);
        }
    }
}
