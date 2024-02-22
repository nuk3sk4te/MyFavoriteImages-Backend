package factoria.org.MyFavoriteImagesBackend.infra.exceptions;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException(String username) {
        super(username + " password is incorrect" );
    }
}
