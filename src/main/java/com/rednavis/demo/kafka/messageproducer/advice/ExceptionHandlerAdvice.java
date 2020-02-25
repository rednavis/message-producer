package com.rednavis.demo.kafka.messageproducer.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.json.JSONObject;
import com.rednavis.demo.kafka.messageproducer.exception.CacheIsEmptyException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  private static final Logger LOGGER = Logger.getLogger(ExceptionHandlerAdvice.class.getName());

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    LOGGER.log(Level.WARNING, e.getMessage());
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new JSONObject(errors).toString();
  }

  @ExceptionHandler(CacheIsEmptyException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No entries found")
  public void handleEmptyCacheException() {
  }

}
