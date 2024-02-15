package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ImageNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteImageServiceTest {
    @Mock
    FavoriteImageRepository imageRepository;

    @InjectMocks
    FavoriteImageService imageService;

    List<FavoriteImage> images;

    @BeforeEach
    void setUp() {
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
        FavoriteImage image = new FavoriteImage();
        image.setId(1L);
        image.setTitle("Image 1");
        image.setDescription("Image 1 description");
        image.setUrl("Image 1 url");

        FavoriteImageUser user = new FavoriteImageUser();
        user.setUsername("User1");

        image.setOwner(user);

        given(imageRepository.findById(1L)).willReturn(Optional.of(image));

        //When
        FavoriteImage returnedImage = imageService.findById(1L);

        //Then
        assertThat(returnedImage.getId()).isEqualTo(1L);
        assertThat(returnedImage.getTitle()).isEqualTo("Image 1");
        assertThat(returnedImage.getDescription()).isEqualTo("Image 1 description");
        assertThat(returnedImage.getUrl()).isEqualTo("Image 1 url");
        assertThat(returnedImage.getOwner().getUsername()).isEqualTo("User1");
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrownErrorWhenImageNotFound() {
        //Given
        given(imageRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()-> {
            FavoriteImage returnedImage = imageService.findById(1L);
        });

        //Then
        assertThat(thrown).isInstanceOf(ImageNotFoundException.class).hasMessage("Could not find image with id: 1");
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindAllSuccessfully() {
        //Given
        given(imageRepository.findAll()).willReturn(this.images);

        //When
        List<FavoriteImage> actualImages = imageService.findAll();

        //Then
        assertThat(actualImages.size()).isEqualTo(this.images.size());
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void shouldSaveSuccessfully(){
        //Given
        FavoriteImage newImage = new FavoriteImage();
        newImage.setTitle("New Image 1");
        newImage.setDescription("New Image 2 description");
        newImage.setUrl("New Image 2 URL");

        given(imageRepository.save(newImage)).willReturn(newImage);

        //When
        FavoriteImage savedImage = imageService.save(newImage);

        //Then
        assertThat(savedImage.getTitle()).isEqualTo("New Image 1");
        assertThat(savedImage.getDescription()).isEqualTo("New Image 2 description");
        assertThat(savedImage.getUrl()).isEqualTo("New Image 2 URL");
        verify(imageRepository, times(1)).save(newImage);
    }

    @Test
    void shouldUpdateSuccessfully() {
        //Given
        FavoriteImage oldImage = new FavoriteImage();
        oldImage.setId(1L);
        oldImage.setTitle("New Image 1");
        oldImage.setDescription("New Image 1 description");
        oldImage.setUrl("New Image 1 URL");

        FavoriteImage updatedImage = new FavoriteImage();
        updatedImage.setId(1L);
        updatedImage.setTitle("New Image 1");
        updatedImage.setDescription("New Image 1 description updated");
        updatedImage.setUrl("New Image 1 URL");

        given(imageRepository.findById(1L)).willReturn(Optional.of(oldImage));
        given(imageRepository.save(oldImage)).willReturn(oldImage);

        //When
        FavoriteImage result = imageService.update(1L, updatedImage);

        //Then
        assertThat(result.getId()).isEqualTo(updatedImage.getId());
        assertThat(result.getDescription()).isEqualTo(updatedImage.getDescription());
        verify(imageRepository, times(1)).findById(1L);
        verify(imageRepository, times(1)).save(oldImage);
    }

    @Test
    void shouldThrownErrorWithNonExistentIdWhenUpdate() {
        //Given
        FavoriteImage updatedImage = new FavoriteImage();
        updatedImage.setId(1L);
        updatedImage.setTitle("New Image 1");
        updatedImage.setDescription("New Image 1 description updated");
        updatedImage.setUrl("New Image 1 URL");

        given(imageRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ImageNotFoundException.class, () -> {
            imageService.update(1L, updatedImage);
        });

        //Then
        verify(imageRepository, times(1)).findById(1L);
    }
}