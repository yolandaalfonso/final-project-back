package dev.yol.final_project_back.user.exceptions;

public class UserAccessDeniedException extends UserException{
    
    public UserAccessDeniedException(String message) {
        super(message);
    }

    public UserAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
