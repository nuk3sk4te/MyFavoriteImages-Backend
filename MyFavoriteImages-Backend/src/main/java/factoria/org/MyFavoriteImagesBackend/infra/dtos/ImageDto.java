package factoria.org.MyFavoriteImagesBackend.infra.dtos;

import jakarta.validation.constraints.NotEmpty;

public record ImageDto(Long id,
                       @NotEmpty(message = "title is required")
                       String title,
                       String description,
                       @NotEmpty(message = "url is required")
                       String url,
                       UserDto owner) {
}
