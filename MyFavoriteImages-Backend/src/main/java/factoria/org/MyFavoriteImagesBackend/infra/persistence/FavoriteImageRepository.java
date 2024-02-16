package factoria.org.MyFavoriteImagesBackend.infra.persistence;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteImageRepository extends JpaRepository<FavoriteImage, Long> {
}
