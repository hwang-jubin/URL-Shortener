package UrlShortener.UrlShortener.exception.customException;


import UrlShortener.UrlShortener.exception.ErrorCode;
import UrlShortener.UrlShortener.exception.customException.RestApiException;
import lombok.Getter;


@Getter
public class BadRequestException extends RestApiException {

    public BadRequestException(String customMessage) {
        super(customMessage , ErrorCode.BAD_REQUEST);
    }

}
