package factoria.org.MyFavoriteImagesBackend.infra;

import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImage;
import factoria.org.MyFavoriteImagesBackend.domain.models.FavoriteImageUser;
import factoria.org.MyFavoriteImagesBackend.domain.services.FavoriteImageUserService;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageRepository;
import factoria.org.MyFavoriteImagesBackend.infra.persistence.FavoriteImageUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {
    private final FavoriteImageRepository imageRepository;
    private final FavoriteImageUserService userService;

    public DBDataInitializer(FavoriteImageRepository imageRepository, FavoriteImageUserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        //Images to be charged once the application starts
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

        FavoriteImage image3 = new FavoriteImage();
        image3.setId(3L);
        image3.setTitle("Image 3");
        image3.setDescription("Description image 3");
        image3.setUrl("image3 URL");

        FavoriteImage image4 = new FavoriteImage();
        image4.setId(4L);
        image4.setTitle("Image 4");
        image4.setDescription("Description image 4");
        image4.setUrl("image4 URL");

        FavoriteImage image5 = new FavoriteImage();
        image5.setId(5L);
        image5.setTitle("Image 5");
        image5.setDescription("Description image 5");
        image5.setUrl("image5 URL");

        //Users to be charged once the application starts
        FavoriteImageUser user1 = new FavoriteImageUser();
        user1.setId(1L);
        user1.setUsername("User1");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");

        FavoriteImageUser user2 = new FavoriteImageUser();
        user2.setId(2L);
        user2.setUsername("User2");
        user2.setPassword("654321");
        user2.setEnabled(true);
        user2.setRoles("admin");

        //Adding images to Users
        user1.addImage(image1);
        user1.addImage(image2);
        user2.addImage(image3);
        user2.addImage(image4);

        //Saving data in DB
        userService.save(user1);
        userService.save(user2);
        imageRepository.save(image5);
    }
}
