package factoria.org.MyFavoriteImagesBackend.controllers;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageService;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ImageNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteImageControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FavoriteImageService imageService;

    List<FavoriteImage> images;

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
        this.mockMvc.perform(get("/api/v1/images/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.title").value("Image 1"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void testFindMyImageByIdNotFound() throws Exception {
        //Given
        given(this.imageService.findById(Long.valueOf("1"))).willThrow(new ImageNotFoundException(1L));

        //When and then
        this.mockMvc.perform(get("/api/v1/images/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find image with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}