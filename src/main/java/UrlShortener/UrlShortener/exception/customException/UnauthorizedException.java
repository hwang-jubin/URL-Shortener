package UrlShortener.UrlShortener.exception.customException;

import UrlShortener.UrlShortener.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RestApiException {

    public UnauthorizedException(String customMessage) {
        super(customMessage , ErrorCode.UNAUTHORIZED_REQUEST);
    }

}
