package UrlShortener.UrlShortener.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * runtime exception 을 상속한 exception 들의 resolver.
 * ErrorResponse로 response를  규격화 하기 위해서 만든 handler
 *
 * ErrorResponse를 내려주는 것으로 해결하고 싶지 않을 때는 다른 handler를 작성해서 연결해야 함
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ErrorResponse> globalExceptionHandler(RestApiException e ){

        //exception에서 온 error를 규격화한 response에 넣음
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getMessage());

        //response를 responseEntity에 넣어서 return
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());

    }

}
