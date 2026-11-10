package org.zuyi.flashreserve.common.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.exception.AuthorizeException;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.common.result.Result;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<Result<Void>> handleBussinessException(
            BussinessException e
    ){
        Result<Void> Body = Result.fail(e.getCode(),e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ){
        Result<Void> body = Result.fail(CodeConstants.ARGS_ERR, MessageConstants.ARGS_ERR);
        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(
            Exception e
    ){
        Result<Void> body = Result.fail(CodeConstants.SERVER_ERR,e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<Result<Void>> handleAuthorizeException(
            AuthorizeException e
    ){
        Result<Void> body = Result.fail(CodeConstants.SERVER_ERR,e.getMessage());
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(body);
    }
}
