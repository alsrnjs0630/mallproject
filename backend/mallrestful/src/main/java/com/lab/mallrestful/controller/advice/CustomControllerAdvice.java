package com.lab.mallrestful.controller.advice;

import com.lab.mallrestful.controller.util.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomControllerAdvice {

    // NoSuchElementException 예외가 발생할 때 해당 메서드가 호출됨
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> notExist(NoSuchElementException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg",msg));
    }

    // MethodArgumentNotValidException 예외가 발생할 때 해당 메서드가 호출됨.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleIlleagalArgumentException(MethodArgumentNotValidException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg",msg));
    }

    // CustomJWTException 예외 발생시 JSON 문자열 전송
    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {
        String msg = e.getMessage();

        return ResponseEntity.ok().body(Map.of("error",msg));
    }
}
