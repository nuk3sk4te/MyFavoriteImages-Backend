package factoria.org.MyFavoriteImagesBackend.controllers;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageService;
import factoria.org.MyFavoriteImagesBackend.infra.results.Result;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteImageController {
    private final FavoriteImageService imageService;

    public FavoriteImageController(FavoriteImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/api/v1/images/{imageId}")
    public Result findImageById(@PathVariable Long imageId){
        FavoriteImage foundImage = this.imageService.findById(imageId);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", foundImage);
    }
}
