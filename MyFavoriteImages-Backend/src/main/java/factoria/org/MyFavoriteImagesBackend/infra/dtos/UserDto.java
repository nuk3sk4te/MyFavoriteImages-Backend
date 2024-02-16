package factoria.org.MyFavoriteImagesBackend.infra.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Long id,
                      @NotEmpty(message = "username is required")
                      String username,
                      boolean enabled,
                      String roles,
                      Integer numberOfImages) {
}
