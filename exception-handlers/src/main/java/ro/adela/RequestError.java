package ro.adela;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestError {

    @JsonProperty("message")
    private final String message;

    public RequestError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
