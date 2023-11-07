package UrlShortener.UrlShortener.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestApiException extends RuntimeException {

    public ErrorCode errorCode;

    public RestApiException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;

    }

}
