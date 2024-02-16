package factoria.org.MyFavoriteImagesBackend.infra.dtos.converters;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.ImageDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageToImageDtoConverter implements Converter<FavoriteImage, ImageDto> {
    private final UserToUserDtoConverter userToUserDtoConverter;

    public ImageToImageDtoConverter(UserToUserDtoConverter userToUserDtoConverter) {
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @Override
    public ImageDto convert(FavoriteImage source) {
        ImageDto imageDto = new ImageDto(source.getId(),
                source.getTitle(),
                source.getDescription(),
                source.getUrl(),
                source.getOwner() != null ? this.userToUserDtoConverter.convert(source.getOwner()) : null);
        return imageDto;
    }
}
