package UrlShortener.UrlShortener.exception.customException;

import UrlShortener.UrlShortener.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomUnauthorizedException extends RestApiException {

    public CustomUnauthorizedException(String customMessage) {
        super(customMessage , ErrorCode.BAD_REQUEST);
    }

}