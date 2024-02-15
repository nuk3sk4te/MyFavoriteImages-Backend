package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ImageNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FavoriteImageService {
    private final FavoriteImageRepository imageRepository;

    public FavoriteImageService(FavoriteImageRepository favoriteImageRepository) {
        this.imageRepository = favoriteImageRepository;
    }

    public FavoriteImage findById(Long imageId) {
        return this.imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));
    }

    public List<FavoriteImage> findAll() {
        return this.imageRepository.findAll();
    }

    public FavoriteImage save(FavoriteImage newImage) {
        return this.imageRepository.save(newImage);
    }

    public FavoriteImage update(Long imageId, FavoriteImage updatedImage) {
        return this.imageRepository.findById(imageId)
                .map(oldImage -> {
                    oldImage.setTitle(updatedImage.getTitle());
                    oldImage.setDescription(updatedImage.getDescription());
                    oldImage.setUrl(updatedImage.getUrl());
                    return this.imageRepository.save(oldImage);
                })
                .orElseThrow(() -> new ImageNotFoundException(imageId));
    }
}
