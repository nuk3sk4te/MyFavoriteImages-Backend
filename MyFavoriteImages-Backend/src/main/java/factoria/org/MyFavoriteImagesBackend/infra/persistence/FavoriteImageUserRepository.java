package factoria.org.MyFavoriteImagesBackend.infra.persistence;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteImageUserRepository extends JpaRepository<FavoriteImageUser, Long> {
}
