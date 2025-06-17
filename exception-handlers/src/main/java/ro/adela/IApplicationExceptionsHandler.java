package ro.adela;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public interface IApplicationExceptionsHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    default ResponseEntity<RequestError> handleGeneralException(NoResourceFoundException ex) {
        RequestError requestError = new RequestError("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(requestError, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    default ResponseEntity<RequestError> handleGeneralException(Exception ex) {
        RequestError requestError = new RequestError("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(requestError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
