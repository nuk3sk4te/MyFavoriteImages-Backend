package factoria.org.MyFavoriteImagesBackend.controllers;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.domain.services.AuthService;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageService;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.ImageDto;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.converters.ImageDtoToImageConverter;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.converters.ImageToImageDtoConverter;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.UserNameNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageUserRepository;
import factoria.org.MyFavoriteImagesBackend.infra.results.Result;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/images")
public class FavoriteImageController {
    private final FavoriteImageService imageService;
    private final ImageToImageDtoConverter imageToImageDtoConverter;
    private final ImageDtoToImageConverter imageDtoToImageConverter;
    private final FavoriteImageUserRepository userRepository;

    public FavoriteImageController(FavoriteImageService imageService, ImageToImageDtoConverter imageToImageDtoConverter, ImageDtoToImageConverter imageDtoToImageConverter, FavoriteImageUserRepository userRepository) {
        this.imageService = imageService;
        this.imageToImageDtoConverter = imageToImageDtoConverter;
        this.imageDtoToImageConverter = imageDtoToImageConverter;
        this.userRepository = userRepository;
    }

    @GetMapping("/{imageId}")
    public Result findImageById(@PathVariable Long imageId){
        FavoriteImage foundImage = this.imageService.findById(imageId);
        ImageDto imageDto = this.imageToImageDtoConverter.convert(foundImage);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", imageDto);
    }

    @GetMapping
    public Result findAllImages() {
        List<FavoriteImage> foundImages = this.imageService.findAll();
        List<ImageDto> imagesDto = foundImages.stream()
                .map(this.imageToImageDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", imagesDto);
    }

    @PostMapping
    public Result addImage(@RequestBody @Valid ImageDto myFavoriteImageDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        FavoriteImageUser userEntity = userRepository.findByUsername(userName).orElseThrow( () -> new UserNameNotFoundException(userName));
        FavoriteImage newImage = this.imageDtoToImageConverter.convert(myFavoriteImageDto);
        assert newImage != null;
        newImage.setOwner(userEntity);
        FavoriteImage savedImage = this.imageService.save(newImage);

        ImageDto savedImageDto = this.imageToImageDtoConverter.convert(savedImage);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedImageDto );
    }

    @PutMapping("/{imageId}")
    public Result updateImage(@PathVariable Long imageId, @Valid @RequestBody ImageDto myFavoriteImageDto) {
        FavoriteImage updatedImage = this.imageDtoToImageConverter.convert(myFavoriteImageDto);
        FavoriteImage result = this.imageService.update(imageId, updatedImage);

        ImageDto updatedImageDto = this.imageToImageDtoConverter.convert(result);
        return new Result(true, StatusCode.SUCCESS, "Updated Success", updatedImageDto );
    }

    @DeleteMapping("/{imageId}")
    public Result deleteImage(@PathVariable Long imageId) {
        this.imageService.delete(imageId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
