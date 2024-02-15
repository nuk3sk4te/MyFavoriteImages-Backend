package factoria.org.MyFavoriteImagesBackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageService;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.ImageDto;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ImageNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteImageControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FavoriteImageService imageService;
    @Autowired
    ObjectMapper objectMapper;
    List<FavoriteImage> images;
    @Value("/api/v1/images")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.images = new ArrayList<>();

        FavoriteImage image1 = new FavoriteImage();
        image1.setId(1L);
        image1.setTitle("Image 1");
        image1.setDescription("Description image 1");
        image1.setUrl("image1 URL");
        this.images.add(image1);

        FavoriteImage image2 = new FavoriteImage();
        image2.setId(2L);
        image2.setTitle("Image 2");
        image2.setDescription("Description image 2");
        image2.setUrl("image2 URL");
        this.images.add(image2);

        FavoriteImage image3 = new FavoriteImage();
        image3.setId(3L);
        image3.setTitle("Image 3");
        image3.setDescription("Description image 3");
        image3.setUrl("image3 URL");
        this.images.add(image3);

        FavoriteImage image4 = new FavoriteImage();
        image4.setId(4L);
        image4.setTitle("Image 4");
        image4.setDescription("Description image 4");
        image4.setUrl("image4 URL");
        this.images.add(image4);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldFindMyImageByIdSuccessfully() throws Exception {
        //Given
        given(this.imageService.findById(1L)).willReturn(this.images.get(0));

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.title").value("Image 1"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void shouldThrownErrorWhenImageNotFound() throws Exception {
        //Given
        given(this.imageService.findById(1L)).willThrow(new ImageNotFoundException(1L));

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find image with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldFindAllImagesSuccessfully() throws Exception {
        //Given
        given(this.imageService.findAll()).willReturn(this.images);

        //When and then
        this.mockMvc.perform(get(this.baseUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.images.size())))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Image 1"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("Image 2"));
    }

    @Test
    void shouldSaveImageSuccessfully() throws Exception {
        //Given
        ImageDto myFavoriteImageDto = new ImageDto(null,
                "New Image",
                "New Image description",
                "New Image URL",
                null);
        String json = this.objectMapper.writeValueAsString(myFavoriteImageDto);

        FavoriteImage savedImage = new FavoriteImage();
        savedImage.setId(1L);
        savedImage.setTitle("New Image");
        savedImage.setDescription("New Image description");
        savedImage.setUrl("New Image URL");

        given(this.imageService.save(Mockito.any(FavoriteImage.class))).willReturn(savedImage);

        //When and then
        this.mockMvc.perform(post(this.baseUrl).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(savedImage.getTitle()))
                .andExpect(jsonPath("$.data.description").value(savedImage.getDescription()))
                .andExpect(jsonPath("$.data.url").value(savedImage.getUrl()));
    }

    @Test
    void shouldUpdateImageSuccessfully() throws Exception {
        //Given
        ImageDto myFavoriteImageDto = new ImageDto(1L,
                "Image 1",
                "Image 1 description updated",
                "Image 1 URL",
                null);
        String json = this.objectMapper.writeValueAsString(myFavoriteImageDto);

        FavoriteImage updatedImage = new FavoriteImage();
        updatedImage.setId(1L);
        updatedImage.setTitle("Image 1");
        updatedImage.setDescription("Image 1 description updated");
        updatedImage.setUrl("Image 1 URL");

        given(this.imageService.update(eq(1L), Mockito.any(FavoriteImage.class))).willReturn(updatedImage);

        //When and then
        this.mockMvc.perform(put(this.baseUrl + "/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Updated Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value(updatedImage.getTitle()))
                .andExpect(jsonPath("$.data.description").value(updatedImage.getDescription()))
                .andExpect(jsonPath("$.data.url").value(updatedImage.getUrl()));
    }

    @Test
    void shouldThrownErrorWithNonExistentImageIdWhenUpdate() throws Exception {
        //Given
        ImageDto myFavoriteImageDto = new ImageDto(1L,
                "Image 1",
                "Image 1 description updated",
                "Image 1 URL",
                null);
        String json = this.objectMapper.writeValueAsString(myFavoriteImageDto);

        given(this.imageService.update(eq(1L), Mockito.any(FavoriteImage.class))).willThrow(new ImageNotFoundException(1L));

        //When and then
        this.mockMvc.perform(put(this.baseUrl + "/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find image with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldDeleteImageSuccessfully() throws Exception {
        //Given
        doNothing().when(this.imageService).delete(1L);

        //When and then
        this.mockMvc.perform(delete(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldThrownErrorWithNonExistentImageIdWhenDelete() throws Exception {
        //Given
        doThrow(new ImageNotFoundException(1L)).when(this.imageService).delete(1L);

        //When and then
        this.mockMvc.perform(delete(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find image with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}