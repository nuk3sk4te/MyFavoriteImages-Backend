package factoria.org.MyFavoriteImagesBackend.infra.exceptions;

public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(Long id) {
        super("Could not find image with id: " + id);
    }
}
