package dev.gollund.gitrepoparser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorMessage handleUnsupportedMediaTypeException(UnsupportedMediaTypeException ex) {
        return new ErrorMessage(HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorMessage(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleGeneralException(Exception ex) {
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error. Reason: " + ex.getLocalizedMessage());
    }

    record ErrorMessage(int status, String Message) {

        ErrorMessage(HttpStatus httpStatus, String Message) {
            this(httpStatus.value(), Message);
        }
    }

}
