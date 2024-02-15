package factoria.org.MyFavoriteImagesBackend.controllers;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageUserService;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.UserDto;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.converters.UserToUserDtoConverter;
import factoria.org.MyFavoriteImagesBackend.infra.results.Result;
import factoria.org.MyFavoriteImagesBackend.infra.results.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class FavoriteImageUserController {
    private final FavoriteImageUserService userService;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public FavoriteImageUserController(FavoriteImageUserService userService, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Long userId){
        FavoriteImageUser foundUser = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @GetMapping
    public Result findAllUsers() {
        List<FavoriteImageUser> foundUsers = this.userService.findAll();
        List<UserDto> usersDto = foundUsers.stream()
                .map(this.userToUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", usersDto);
    }
}
