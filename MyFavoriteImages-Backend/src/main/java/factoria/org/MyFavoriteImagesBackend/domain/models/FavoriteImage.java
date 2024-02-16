package factoria.org.MyFavoriteImagesBackend.domain.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
@Entity
public class FavoriteImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String url;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FavoriteImageUser owner;

    public FavoriteImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FavoriteImageUser getOwner() {
        return owner;
    }

    public void setOwner(FavoriteImageUser owner) {
        this.owner = owner;
    }
}
