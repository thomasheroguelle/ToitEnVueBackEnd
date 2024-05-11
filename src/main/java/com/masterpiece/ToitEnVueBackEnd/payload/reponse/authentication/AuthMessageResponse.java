package com.masterpiece.ToitEnVueBackEnd.payload.reponse.authentication;

public class AuthMessageResponse {
    private String message;

    public AuthMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
