package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ObjectNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FavoriteImageUserService {
    private final FavoriteImageUserRepository userRepository;

    public FavoriteImageUserService(FavoriteImageUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public FavoriteImageUser findById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<FavoriteImageUser> findAll() {
        return this.userRepository.findAll();
    }

    public FavoriteImageUser save(FavoriteImageUser newUser) {
        return this.userRepository.save(newUser);
    }

    public FavoriteImageUser update(Long userId, FavoriteImageUser updatedUser) {
        return this.userRepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setUsername(updatedUser.getUsername());
                    oldUser.setEnabled(updatedUser.isEnabled());
                    oldUser.setRoles(updatedUser.getRoles());
                    return this.userRepository.save(oldUser);
                })
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public void delete(Long userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }
}
