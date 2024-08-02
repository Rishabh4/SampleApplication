package com.rishabh.SampleApplication.exception;

import com.rishabh.SampleApplication.model.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleAppException(CustomException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDto(ex.getMessage()));
    }
}
