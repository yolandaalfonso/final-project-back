package dev.yol.final_project_back.profile;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.yol.final_project_back.user.UserEntity;

public class ProfileEntityTest {
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id_user(1L)
                .email("user@example.com")
                .password("securePass")
                .uid("uid123")
                .build();
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProfileEntity profile = new ProfileEntity();
        profile.setIdProfile(1L);
        profile.setUserName("jdoe");
        profile.setName("John");
        profile.setFirstSurname("Doe");
        profile.setBio("Hello world");
        profile.setAvatar("/avatars/john.png");
        profile.setUser(user);

        assertThat(profile.getIdProfile(), is(1L));
        assertThat(profile.getUserName(), is("jdoe"));
        assertThat(profile.getName(), is("John"));
        assertThat(profile.getFirstSurname(), is("Doe"));
        assertThat(profile.getBio(), is("Hello world"));
        assertThat(profile.getAvatar(), is("/avatars/john.png"));
        assertThat(profile.getUser(), is(user));
    }

    @Test
    void testAllArgsConstructor() {
        ProfileEntity profile = new ProfileEntity(
                2L,
                "maria22",
                "María",
                "González",
                "Frontend developer",
                "/avatars/maria.png",
                user
        );

        assertThat(profile.getIdProfile(), is(2L));
        assertThat(profile.getUserName(), is("maria22"));
        assertThat(profile.getName(), is("María"));
        assertThat(profile.getFirstSurname(), is("González"));
        assertThat(profile.getBio(), is("Frontend developer"));
        assertThat(profile.getAvatar(), is("/avatars/maria.png"));
        assertThat(profile.getUser(), is(user));
    }

    @Test
    void testBuilder() {
        ProfileEntity profile = ProfileEntity.builder()
                .idProfile(3L)
                .userName("carlosdev")
                .name("Carlos")
                .firstSurname("Ruiz")
                .bio("Fullstack engineer")
                .avatar("/avatars/carlos.png")
                .user(user)
                .build();

        assertThat(profile.getIdProfile(), is(3L));
        assertThat(profile.getUserName(), is("carlosdev"));
        assertThat(profile.getName(), is("Carlos"));
        assertThat(profile.getFirstSurname(), is("Ruiz"));
        assertThat(profile.getBio(), is("Fullstack engineer"));
        assertThat(profile.getAvatar(), is("/avatars/carlos.png"));
        assertThat(profile.getUser(), is(user));
    }

    @Test
    void testDefaultAvatarValue() {
        ProfileEntity profile = new ProfileEntity();
        assertThat(profile.getAvatar(), is("/avatars/default-avatar.png"));
    }
}
