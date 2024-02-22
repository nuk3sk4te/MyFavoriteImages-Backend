package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.MyUserPrincipal;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ObjectNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.UserNameNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class FavoriteImageUserService implements UserDetailsService {
    private final FavoriteImageUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteImageService imageService;

    public FavoriteImageUserService(FavoriteImageUserRepository userRepository, PasswordEncoder passwordEncoder, FavoriteImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    public FavoriteImageUser findById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<FavoriteImageUser> findAll() {
        return this.userRepository.findAll();
    }

    public FavoriteImageUser save(FavoriteImageUser newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
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

    public List<FavoriteImage> getUserImages(Long userId) {
        FavoriteImageUser user = findById(userId);
        return user.getImages();

    }

    public void deleteImageByUser(Long userId, Long imageId) {
        FavoriteImageUser foundUser = findById(userId);
        FavoriteImage imageToRemove = foundUser.getImages().stream()
                .filter(image -> Objects.equals(image.getId(), imageId))
                .findFirst()
                .orElse(null);
        foundUser.getImages().remove(imageToRemove);
        this.userRepository.save(foundUser);
        this.imageService.delete(imageId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UserNameNotFoundException(username));
    }
}
