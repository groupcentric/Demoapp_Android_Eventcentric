package com.eventcentric.helper;

public class ErrorResponse {
    private int errorCode;
    private String errorMessage;
    private String buttonTitle;
    private String buttonAction;
    private Exception exception;
    private boolean fatalError;

    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        if (errorMessage == null && exception != null)
            return exception.getLocalizedMessage();
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getButtonTitle() {
        return buttonTitle;
    }
    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }
    public String getButtonAction() {
        return buttonAction;
    }
    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }
    public void setException(Exception exception) {
        this.exception = exception;
    }
    public Exception getException() {
        return exception;
    }
    public boolean isEmpty() {
        return exception == null && getErrorMessage() == null;
    }
    public void setFatalError(boolean fatalError) {
        this.fatalError = fatalError;
    }
    public boolean isFatalError() {
        return fatalError;
    }
}
