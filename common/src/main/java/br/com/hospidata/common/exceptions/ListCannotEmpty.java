package br.com.hospidata.common.exceptions;

public class ListCannotEmpty extends RuntimeException {


    public ListCannotEmpty() {
        super("Requests cannot be empty");
    }
}
