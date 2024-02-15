package factoria.org.MyFavoriteImagesBackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageUserService;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ObjectNotFoundException;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class FavoriteImageUserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FavoriteImageUserService userService;
    List<FavoriteImageUser> users;

    @Value("/api/v1/users")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        FavoriteImageUser user1 = new FavoriteImageUser();
        user1.setId(1L);
        user1.setUsername("User1");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");
        this.users.add(user1);

        FavoriteImageUser user2 = new FavoriteImageUser();
        user2.setId(2L);
        user2.setUsername("User2");
        user2.setPassword("654321");
        user2.setEnabled(true);
        user2.setRoles("admin");
        this.users.add(user2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldFindUserByIdSuccessfully() throws Exception {
        //Given
        given(this.userService.findById(1L)).willReturn(this.users.get(0));

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.username").value("User1"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void shouldThrownErrorWhenUserNotFound() throws Exception {
        //Given
        given(this.userService.findById(1L)).willThrow(new ObjectNotFoundException("user", 1L));

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldFindAllUsersSuccessfully() throws Exception {
        //Given
        given(this.userService.findAll()).willReturn(this.users);

        //When and then
        this.mockMvc.perform(get(this.baseUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("User1"))
                .andExpect(jsonPath("$.data[1].id").value(2l))
                .andExpect(jsonPath("$.data[1].username").value("User2"));
    }

}