package dev.yol.final_project_back.user.exceptions;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;


public class UserException extends RuntimeException{
    
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
