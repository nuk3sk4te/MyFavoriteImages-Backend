package factoria.org.MyFavoriteImagesBackend.infra.dtos.converters;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<FavoriteImageUser, UserDto> {
    @Override
    public UserDto convert(FavoriteImageUser source) {
        return new UserDto(source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles(),
                source.getNumberOfImages());
    }
}
