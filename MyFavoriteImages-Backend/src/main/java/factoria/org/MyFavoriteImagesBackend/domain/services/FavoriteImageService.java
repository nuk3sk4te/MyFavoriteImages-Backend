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
    private final FavoriteImageRepository favoriteImageRepository;

    public FavoriteImageService(FavoriteImageRepository favoriteImageRepository) {
        this.favoriteImageRepository = favoriteImageRepository;
    }

    public FavoriteImage findById(Long imageId) {
        return this.favoriteImageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));
    }

    public List<FavoriteImage> findAll() {
        return this.favoriteImageRepository.findAll();
    }
}
