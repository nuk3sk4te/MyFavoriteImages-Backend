package factoria.org.MyFavoriteImagesBackend.domain.services;

import factoria.org.MyFavoriteImagesBackend.config.security.JwtProvider;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.infra.MyUserPrincipal;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.UserDto;
import factoria.org.MyFavoriteImagesBackend.infra.dtos.converters.UserToUserDtoConverter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal)authentication.getPrincipal();
        FavoriteImageUser user = principal.getUser();
        UserDto userDto = this.userToUserDtoConverter.convert(user);

        String token = this.jwtProvider.createToken(authentication);
        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
