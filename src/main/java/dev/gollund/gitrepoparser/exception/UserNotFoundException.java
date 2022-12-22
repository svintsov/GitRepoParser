package dev.gollund.gitrepoparser.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String name) {
        super(String.format("Username: %s was not found", name));
    }
}
