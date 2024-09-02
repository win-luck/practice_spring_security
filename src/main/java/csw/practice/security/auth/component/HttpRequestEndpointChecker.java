package csw.practice.security.auth.component;

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

    public boolean isEndpointExist(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : servlet.getHandlerMappings()) {
            try {
                if (handlerMapping instanceof RequestMappingHandlerMapping) {
                    HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
                    if (foundHandler != null) {
                        log.info("Endpoint exists: {}", request.getRequestURI());
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
