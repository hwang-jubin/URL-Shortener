package UrlShortener.UrlShortener.exception;

import lombok.Getter;

@Getter
public class InvalidUrlException extends RuntimeException{

    public ErrorCode errorCode;

    public InvalidUrlException(String customMessage, ErrorCode errorCode) {
        super(customMessage);
        this.errorCode = errorCode;
    }
    }

