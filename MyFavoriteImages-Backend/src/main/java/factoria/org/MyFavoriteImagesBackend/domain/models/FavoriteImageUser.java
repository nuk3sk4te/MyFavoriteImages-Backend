package factoria.org.MyFavoriteImagesBackend.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FavoriteImageUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private String roles;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner", fetch=FetchType.EAGER)
    private List<FavoriteImage> images = new ArrayList<>();

    public FavoriteImageUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<FavoriteImage> getImages() {
        return images;
    }

    public void setImages(List<FavoriteImage> images) {
        this.images = images;
    }

    public void addImage(FavoriteImage image) {
        image.setOwner(this);
        this.images.add(image);
    }

    public Integer getNumberOfImages() {
        return this.images.size();
    }
}
