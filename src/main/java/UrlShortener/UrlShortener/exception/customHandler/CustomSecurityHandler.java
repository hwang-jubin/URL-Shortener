package UrlShortener.UrlShortener.exception.customHandler;

import UrlShortener.UrlShortener.exception.ErrorResponse;
import UrlShortener.UrlShortener.exception.customException.SecurityUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomSecurityHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException instanceof SecurityUnauthorizedException) {
            // MyCustomAccessDeniedException에 특화된 처리

            SecurityUnauthorizedException securityUnauthorizedException = (SecurityUnauthorizedException) accessDeniedException;
            log.info("error message: "+securityUnauthorizedException.getMessage());
            //exception에서 온 error를 규격화한 response에 넣음
            ErrorResponse errorResponse = new ErrorResponse(securityUnauthorizedException.getErrorCode().getHttpStatus(), securityUnauthorizedException.getMessage());
            //response를 responseEntity에 넣어서 return
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());

            response.getWriter().write(responseEntity.toString());
            response.setStatus(HttpStatus.FORBIDDEN.value());

        } else {
            // 일반적인 AccessDeniedException에 대한 처리
            response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        }
    }
}
