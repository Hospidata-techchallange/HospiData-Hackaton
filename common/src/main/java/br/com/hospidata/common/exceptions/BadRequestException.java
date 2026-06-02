package br.com.hospidata.common.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String resource, String field, String detail) {
        super(String.format("Invalid request for %s: %s (%s)", resource, field, detail));
    }

}
