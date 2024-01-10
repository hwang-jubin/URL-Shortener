package UrlShortener.UrlShortener.exception.customException;

import UrlShortener.UrlShortener.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;

@Getter
public class SecurityUnauthorizedException extends AccessDeniedException {

    ErrorCode errorCode;

    public SecurityUnauthorizedException(String errorMessage) {
        super(errorMessage);
    }

    public SecurityUnauthorizedException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public SecurityUnauthorizedException(String errorMessage , ErrorCode errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
