package UrlShortener.UrlShortener.exception;

import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.exception.customException.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponse> invalidUrlExceptionHandler(BadRequestException e){

        log.info(e.getMessage());
        //exception에서 온 error를 규격화한 response에 넣음
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getMessage());

        //response를 responseEntity에 넣어서 return
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
