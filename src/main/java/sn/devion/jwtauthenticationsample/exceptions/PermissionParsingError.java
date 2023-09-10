package sn.devion.jwtauthenticationsample.exceptions;


/**
 * Exception thrown when a permission is not well formatted
 */


public class PermissionParsingError extends RuntimeException {
    public PermissionParsingError(String message) {
        super(message);
    }
}
