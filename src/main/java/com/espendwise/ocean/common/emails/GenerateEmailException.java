package com.espendwise.ocean.common.emails;


public class GenerateEmailException extends RuntimeException{
  
    private GenerateEmailError error; 
    
    public GenerateEmailException() {
    }

    public GenerateEmailException(GenerateEmailError error) {
        this.error = error;
    }


    public GenerateEmailException(String message, Throwable cause, GenerateEmailError error) {
        super(message, cause);
        this.error = error;
    }

    public GenerateEmailException(Throwable cause, GenerateEmailError error) {
        super(cause);
        this.error = error;
    }

    public GenerateEmailError getError() {
        return error;
    }
}
