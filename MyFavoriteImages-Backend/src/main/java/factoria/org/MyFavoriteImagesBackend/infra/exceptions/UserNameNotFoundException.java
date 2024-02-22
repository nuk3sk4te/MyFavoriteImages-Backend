package factoria.org.MyFavoriteImagesBackend.infra.exceptions;

public class UserNameNotFoundException extends RuntimeException{
    public UserNameNotFoundException(String username) {
        super("Username " + username + " is not found" );
    }
}
