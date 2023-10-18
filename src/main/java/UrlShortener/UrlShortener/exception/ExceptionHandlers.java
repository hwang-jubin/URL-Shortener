package UrlShortener.UrlShortener.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler(InvalidUrlException.class)
    protected ResponseEntity<ErrorResponse> invalidUrlExceptionHandler(InvalidUrlException e){

        //exception에서 온 error를 규격화한 response에 넣음
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getMessage());

        //response를 responseEntity에 넣어서 return
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
