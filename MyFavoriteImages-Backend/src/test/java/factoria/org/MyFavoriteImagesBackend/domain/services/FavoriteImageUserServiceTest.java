package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ObjectNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteImageUserServiceTest {
    @Mock
    FavoriteImageUserRepository userRepository;

    @Mock
    FavoriteImageService imageService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    FavoriteImageUserService userService;

    List<FavoriteImageUser> users;

    List<FavoriteImage> images;

    @BeforeEach
    void setUp() {
        FavoriteImageUser user1 = new FavoriteImageUser();
        user1.setId(1L);
        user1.setUsername("User1");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");

        FavoriteImageUser user2 = new FavoriteImageUser();
        user2.setId(2L);
        user2.setUsername("User2");
        user2.setPassword("654321");
        user2.setEnabled(true);
        user2.setRoles("admin");

        this.users = new ArrayList<>();
        this.users.add(user1);
        this.users.add(user2);

        FavoriteImage image1 = new FavoriteImage();
        image1.setId(1L);
        image1.setTitle("Image 1");
        image1.setDescription("Description image 1");
        image1.setUrl("image1 URL");

        FavoriteImage image2 = new FavoriteImage();
        image2.setId(2L);
        image2.setTitle("Image 2");
        image2.setDescription("Description image 2");
        image2.setUrl("image2 URL");

        this.images = new ArrayList<>();
        this.images.add(image1);
        this.images.add(image2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldFindByIdSuccessfully() {
        FavoriteImage image1 = new FavoriteImage();
        image1.setId(Long.valueOf("1"));
        image1.setTitle("Image 1");
        image1.setDescription("Description image 1");
        image1.setUrl("image1 URL");

        FavoriteImageUser user = new FavoriteImageUser();
        user.setId(1L);
        user.setUsername("User 1");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("user");
        user.addImage(image1);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //When
        FavoriteImageUser returnedUser = userService.findById(1L);

        //Then
        assertThat(returnedUser.getId()).isEqualTo(1L);
        assertThat(returnedUser.getUsername()).isEqualTo("User 1");
        assertThat(returnedUser.getPassword()).isEqualTo("123456");
        assertThat(returnedUser.isEnabled()).isEqualTo(true);
        assertThat(returnedUser.getRoles()).isEqualTo("user");
        assertThat(returnedUser.getImages()).isNotEmpty();
        assertThat(returnedUser.getImages()).containsExactly(image1);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrownErrorWhenUserNotFound() {
        //Given
        given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()-> {
            FavoriteImageUser returnedUser = userService.findById(1L);
        });

        //Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id: 1");
        verify(userRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void shouldFindAllSuccessfully() {
        //Given
        given(userRepository.findAll()).willReturn(this.users);

        //When
        List<FavoriteImageUser> actualUsers = userService.findAll();

        //Then
        assertThat(actualUsers.size()).isEqualTo(this.users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldSaveSuccessfully(){
        //Given
        FavoriteImageUser newUser = new FavoriteImageUser();
        newUser.setUsername("New User");
        newUser.setPassword("987654");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        given(userRepository.save(ArgumentMatchers.any(FavoriteImageUser.class))).willReturn(newUser);

        //When
        FavoriteImageUser savedUser = userService.save(newUser);

        //Then
        assertThat(savedUser.getUsername()).isEqualTo("New User");
        assertThat(savedUser.isEnabled()).isEqualTo(true);
        assertThat(savedUser.getRoles()).isEqualTo("user");
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void shouldUpdateSuccessfully() {
        //Given
        FavoriteImageUser oldUser = new FavoriteImageUser();
        oldUser.setUsername("New User 1");
        oldUser.setEnabled(true);
        oldUser.setRoles("user");

        FavoriteImageUser updatedUser = new FavoriteImageUser();
        updatedUser.setUsername("New User 1 updated");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("user");

        given(userRepository.findById(1L)).willReturn(Optional.of(oldUser));
        given(userRepository.save(oldUser)).willReturn(oldUser);

        //When
        FavoriteImageUser result = userService.update(1L, updatedUser);

        //Then
        assertThat(result.getId()).isEqualTo(updatedUser.getId());
        assertThat(result.getUsername()).isEqualTo(updatedUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(oldUser);
    }

    @Test
    void shouldThrownErrorWithNonExistentIdWhenUpdate() {
        //Given
        FavoriteImageUser updatedUser = new FavoriteImageUser();
        updatedUser.setUsername("New User 1 updated");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("user");

        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            userService.update(1L, updatedUser);
        });

        //Then
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindAllImagesByUserSuccessfully() throws Exception {
        //Given
        FavoriteImageUser user = new FavoriteImageUser();
        user.setId(1L);
        user.setUsername("User1");

        FavoriteImage image1 = new FavoriteImage();
        image1.setId(1L);
        image1.setTitle("Image 1");
        image1.setDescription("Image 1 description");
        image1.setUrl("Image 1 URL");

        FavoriteImage image2 = new FavoriteImage();
        image2.setId(2L);
        image2.setTitle("Image 2");
        image2.setDescription("Image 2 description");
        image2.setUrl("Image 2 URL");

        user.addImage(image1);
        user.addImage(image2);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //When
        List<FavoriteImage> images = userService.getUserImages(user.getId());

        //Then
        assertThat(user.getImages().size()).isEqualTo(2);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void shouldDeleteSuccessfully() {
        //Given
        FavoriteImageUser user = new FavoriteImageUser();
        user.setId(1L);
        user.setUsername("User");
        user.setEnabled(true);
        user.setRoles("user");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        //When
        userService.delete(1L);

        //Then
        verify(userRepository, times(1)).deleteById(Long.valueOf("1"));
    }

    @Test
    void shouldThrownErrorWithNonExistentIdWhenDelete() {
        //Given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,() -> {
            userService.delete(1L);
        });

        //Then
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteByUserSuccessfully() {
        //Given
        FavoriteImageUser user = new FavoriteImageUser();
        user.setId(1L);
        user.setUsername("User");
        user.setEnabled(true);
        user.setRoles("user");

        FavoriteImage image1 = new FavoriteImage();
        image1.setId(1L);
        image1.setTitle("Image 1");
        image1.setDescription("Image 1 description");
        image1.setUrl("Image 1 URL");

        FavoriteImage image2 = new FavoriteImage();
        image2.setId(2L);
        image2.setTitle("Image 2");
        image2.setDescription("Image 2 description");
        image2.setUrl("Image 2 URL");

        user.addImage(image1);
        user.addImage(image2);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //When
        userService.deleteImageByUser(1L, 1L);

        //Then
        assertThat(user.getNumberOfImages()).isEqualTo(1);
        verify(userRepository, times(1)).findById(1L);
        verify(imageService, times(1)).delete(1L);
    }

    @Test
    void shouldThrownErrorWithNonExistentIdWhenDeleteByUser() {
        //Given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,() -> {
            userService.deleteImageByUser(1L, 1L);
        });

        //Then
        verify(userRepository, times(1)).findById(1L);
    }
}