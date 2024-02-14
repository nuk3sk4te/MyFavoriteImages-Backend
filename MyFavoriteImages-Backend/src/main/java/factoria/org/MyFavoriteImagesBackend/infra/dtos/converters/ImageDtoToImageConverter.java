package factoria.org.MyFavoriteImagesBackend.infra.dtos.converters;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.ImageDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageDtoToImageConverter implements Converter<ImageDto, FavoriteImage> {
    @Override
    public FavoriteImage convert(ImageDto source) {
        FavoriteImage image = new FavoriteImage();
        image.setId(source.id());
        image.setTitle(source.title());
        image.setDescription(source.description());
        image.setUrl(source.imageUrl());
        return image;
    }
}
