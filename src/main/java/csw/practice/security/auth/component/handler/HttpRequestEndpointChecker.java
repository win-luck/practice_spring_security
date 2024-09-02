package csw.practice.security.auth.component.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpRequestEndpointChecker {

    private final DispatcherServlet servlet;

    // URI 엔드포인트 존재 여부 확인하여 Spring Security가 403을 띄우기 전에 404를 감지하도록 함
    public boolean isEndpointExist(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : servlet.getHandlerMappings()) {
            try {
                if (handlerMapping instanceof RequestMappingHandlerMapping) {
                    HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
                    if (foundHandler != null) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
