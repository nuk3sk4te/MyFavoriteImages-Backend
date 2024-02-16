package factoria.org.MyFavoriteImagesBackend.infra.dtos.converters;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, FavoriteImageUser> {
    @Override
    public FavoriteImageUser convert(UserDto source) {
        FavoriteImageUser imageUser = new FavoriteImageUser();
        imageUser.setUsername(source.username());
        imageUser.setEnabled(source.enabled());
        imageUser.setRoles(source.roles());
        return imageUser;
    }
}
