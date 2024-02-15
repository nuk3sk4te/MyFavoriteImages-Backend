package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ObjectNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FavoriteImageUserServiceTest {
    @Mock
    FavoriteImageUserRepository userRepository;

    @InjectMocks
    FavoriteImageUserService userService;

    @BeforeEach
    void setUp() {
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
            FavoriteImageUser returnedImage = userService.findById(1L);
        });

        //Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id: 1");
        verify(userRepository, times(1)).findById(Long.valueOf("1"));
    }
}