package factoria.org.MyFavoriteImagesBackend.infra;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {
    private final FavoriteImageUser user;

    public MyUserPrincipal(FavoriteImageUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(this.user.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public FavoriteImageUser getUser() {
        return user;
    }

    public List<FavoriteImage> getUserImages() {
        return this.user.getImages();
    }
}
