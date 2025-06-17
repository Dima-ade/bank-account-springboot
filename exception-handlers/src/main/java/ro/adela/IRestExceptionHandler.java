package ro.adela;

import jakarta.xml.bind.JAXBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.adela.bank.exceptions.JsonProviderException;

import java.io.IOException;

public interface IRestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    default ResponseEntity<RequestError> handleIllegalArgumentException(IllegalArgumentException ex) {
        RequestError requestError = new RequestError(ex.getMessage());
        return new ResponseEntity<>(requestError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProviderException.class)
    default ResponseEntity<RequestError> handleJsonProviderException(JsonProviderException ex) {
        RequestError requestError = new RequestError(ex.getMessage());
        return new ResponseEntity<>(requestError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JAXBException.class)
    default ResponseEntity<RequestError> handleJAXBException(JAXBException ex) {
        RequestError requestError = new RequestError(ex.getMessage());
        return new ResponseEntity<>(requestError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    default ResponseEntity<RequestError> handleIOException(IOException ex) {
        RequestError requestError = new RequestError(ex.getMessage());
        return new ResponseEntity<>(requestError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
