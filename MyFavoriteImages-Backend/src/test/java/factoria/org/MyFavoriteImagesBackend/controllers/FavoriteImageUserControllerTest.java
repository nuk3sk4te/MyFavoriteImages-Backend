package factoria.org.MyFavoriteImagesBackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageUserService;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.UserDto;
import factoria.org.MyFavoriteImagesBackend.infra.exceptions.ObjectNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteImageUserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FavoriteImageUserService userService;
    @Autowired
    ObjectMapper objectMapper;
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

    @Test
    void shouldSaveUserSuccessfully() throws Exception {
        //Given
        UserDto userDto = new UserDto(null,
                "New User",
                true,
                "user",
                null);
        String json = this.objectMapper.writeValueAsString(userDto);

        FavoriteImageUser savedUser = new FavoriteImageUser();
        savedUser.setId(1L);
        savedUser.setUsername("New User");
        savedUser.setEnabled(true);
        savedUser.setRoles("user");

        given(this.userService.save(Mockito.any(FavoriteImageUser.class))).willReturn(savedUser);

        //When and then
        this.mockMvc.perform(post(this.baseUrl).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value(savedUser.getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(savedUser.isEnabled()))
                .andExpect(jsonPath("$.data.roles").value(savedUser.getRoles()));
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        //Given
        UserDto userDto = new UserDto(1L,
                "User 1 updated",
                true,
                "user",
                null);
        String json = this.objectMapper.writeValueAsString(userDto);

        FavoriteImageUser updatedUser = new FavoriteImageUser();
        updatedUser.setId(1L);
        updatedUser.setUsername("User 1");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("user");

        given(this.userService.update(eq(1L), Mockito.any(FavoriteImageUser.class))).willReturn(updatedUser);

        //When and then
        this.mockMvc.perform(put(this.baseUrl + "/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Updated Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(updatedUser.isEnabled()))
                .andExpect(jsonPath("$.data.roles").value(updatedUser.getRoles()));
    }

    @Test
    void shouldThrownErrorWithNonExistentUserIdWhenUpdate() throws Exception {
        //Given
        UserDto userDto = new UserDto(1L,
                "User 1",
                true,
                "user",
                null);
        String json = this.objectMapper.writeValueAsString(userDto);

        given(this.userService.update(eq(1L), Mockito.any(FavoriteImageUser.class))).willThrow(new ObjectNotFoundException("user", 1L));

        //When and then
        this.mockMvc.perform(put(this.baseUrl + "/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        //Given
        doNothing().when(this.userService).delete(1L);

        //When and then
        this.mockMvc.perform(delete(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldThrownErrorWithNonExistentUserIdWhenDelete() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("user", 1L)).when(this.userService).delete(1L);

        //When and then
        this.mockMvc.perform(delete(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}