package factoria.org.MyFavoriteImagesBackend.controllers;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageService;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.ImageDto;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.converters.ImageToImageDtoConverter;
import factoria.org.MyFavoriteImagesBackend.infra.results.Result;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteImageController {
    private final FavoriteImageService imageService;
    private final ImageToImageDtoConverter imageToImageDtoConverter;

    public FavoriteImageController(FavoriteImageService imageService, ImageToImageDtoConverter imageToImageDtoConverter) {
        this.imageService = imageService;
        this.imageToImageDtoConverter = imageToImageDtoConverter;
    }

    @GetMapping("/api/v1/images/{imageId}")
    public Result findImageById(@PathVariable Long imageId){
        FavoriteImage foundImage = this.imageService.findById(imageId);
        ImageDto imageDto = this.imageToImageDtoConverter.convert(foundImage);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", imageDto);
    }
}
