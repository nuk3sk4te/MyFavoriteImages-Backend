package factoria.org.MyFavoriteImagesBackend.infra.dtos;

public record ImageDto(Long id,
                       String title,
                       String description,
                       String imageUrl,
                       UserDto owner) {
}
