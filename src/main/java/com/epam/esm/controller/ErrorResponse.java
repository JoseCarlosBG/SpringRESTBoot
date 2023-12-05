package com.epam.esm.controller;

public class ErrorResponse {
    private String errorMessage;
    private int errorCode;
    private Object errorDetails;  // Add this field for additional details

    public ErrorResponse(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
    public String getErrorMessage(){
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage){
        this.errorMessage=errorMessage;
    }
    public int getErrorCode(){
        return errorCode;
    }
    public void setErrorCode(int errorCode){
        this.errorCode=errorCode;
    }
}