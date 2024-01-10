package UrlShortener.UrlShortener.exception.customException;

import UrlShortener.UrlShortener.exception.ErrorCode;
import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class RestApiException extends RuntimeException{

    ErrorCode errorCode;

    public RestApiException(String customMessage , ErrorCode errorCode) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
