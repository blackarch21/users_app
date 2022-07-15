package com.example.accounting_app.ui.response;

public enum ErrorMessages {

    MISSING_REQUIRED_FIELD("Missing required field please check documentation"),
    RECORD_ALREDY_EXISTS("Record alredy exists"),
    INTERNAL_SERVER_ERROR("internal server error");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
