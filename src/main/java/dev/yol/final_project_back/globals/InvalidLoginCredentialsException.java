package dev.yol.final_project_back.globals;

public class InvalidLoginCredentialsException extends RuntimeException{
    
    public InvalidLoginCredentialsException() {
        super("Invalid login credentials");
    }

    public InvalidLoginCredentialsException(String message) {
        super(message);
    }
    
}
