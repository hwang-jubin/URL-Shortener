package UrlShortener.UrlShortener.exception.customException;


import UrlShortener.UrlShortener.exception.ErrorCode;
import UrlShortener.UrlShortener.exception.RestApiException;

public class BadRequestException extends RestApiException {

    public BadRequestException(String customMessage) {
        super(ErrorCode.BAD_REQUEST, customMessage);
    }
}
