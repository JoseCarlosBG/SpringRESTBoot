package com.epam.esm.controller;

public class PageNotFoundException extends RuntimeException {
    private Object requestedPageNumber;

    public PageNotFoundException(String message, Object requestedPageNumber) {
        super(message);
        this.requestedPageNumber = requestedPageNumber;
    }

    public Object getRequestedPageNumber() {
        return requestedPageNumber;
    }
}