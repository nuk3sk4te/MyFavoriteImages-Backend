package factoria.org.MyFavoriteImagesBackend.infra.dtos;

public record UserDto(Long id,
                      String username,
                      boolean enabled,
                      String roles,
                      Integer numberOfImages) {
}
