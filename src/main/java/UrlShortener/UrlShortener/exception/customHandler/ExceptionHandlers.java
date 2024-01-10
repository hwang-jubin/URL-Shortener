package UrlShortener.UrlShortener.exception.customHandler;

import UrlShortener.UrlShortener.exception.ErrorResponse;
import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponse> badRequestExceptionHandler(BadRequestException e){

        log.info("error message: "+e.getMessage());
        //exception에서 온 error를 규격화한 response에 넣음
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getMessage());
        //response를 responseEntity에 넣어서 return
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> CustomUnauthorizedExceptionHandler(UnauthorizedException e){

        log.info("error message: "+e.getMessage());
        //exception에서 온 error를 규격화한 response에 넣음
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getMessage());
        //response를 responseEntity에 넣어서 return
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

}
