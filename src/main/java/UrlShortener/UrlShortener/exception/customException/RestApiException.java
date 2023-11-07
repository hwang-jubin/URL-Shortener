package UrlShortener.UrlShortener.exception.customException;

import UrlShortener.UrlShortener.exception.ErrorCode;
import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class RestApiException extends RuntimeException{

    ErrorCode errorCode = ErrorCode.BAD_REQUEST;


    public RestApiException(String customMessage) {
        super(customMessage);
    }




}
