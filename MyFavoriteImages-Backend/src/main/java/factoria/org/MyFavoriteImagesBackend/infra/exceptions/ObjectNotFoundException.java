package factoria.org.MyFavoriteImagesBackend.infra.exceptions;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectName, Long id) {
        super("Could not find "+ objectName +  " with id: " + id);
    }
}
