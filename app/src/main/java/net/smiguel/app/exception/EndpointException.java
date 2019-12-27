package net.smiguel.app.exception;

import retrofit2.Response;

public class EndpointException extends RuntimeException {

    private final Response response;

    public EndpointException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
