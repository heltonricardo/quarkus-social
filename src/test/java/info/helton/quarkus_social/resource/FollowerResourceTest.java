package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import info.helton.quarkus_social.dto.input.FollowerIDTO;
import info.helton.quarkus_social.dto.output.FollowerODTO;
import info.helton.quarkus_social.error.ResponseError;
import info.helton.quarkus_social.model.Follower;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.FollowerRepository;
import info.helton.quarkus_social.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
class PostResourceTest {

    final UserRepository userRepository;
    final FollowerRepository followerRepository;

    Long userId;
    Long followerId;
    Long inexistentUserId = 999L;

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setAge(30);
        user.setName("Ana");
        userRepository.persist(user);
        userId = user.getId();

        User follower = new User();
        follower.setName("Bia");
        follower.setAge(25);
        userRepository.persist(follower);
        followerId = follower.getId();

        Follower follow = new Follower();
        follow.setUser(user);
        follow.setFollower(follower);
        followerRepository.persist(follow);
    }

    @AfterEach
    @Transactional
    void deleteUsers() {
        followerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    void followValidationErrorTest() {
        FollowerIDTO dto = new FollowerIDTO();
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", userId)
                .when().put()
                .then().statusCode(ResponseError.UNPROCESSABLE_ENTITY);
    }

    @Test
    @DisplayName("Should return 409 when followerId is equal to userId")
    void sameUserAsFollowerTest() {
        FollowerIDTO dto = new FollowerIDTO();
        dto.setFollowerId(userId);
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", userId)
                .when().put()
                .then().statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 on follow an user when user does not exist")
    void userNotFoundWhenTryToFollowTest() {
        FollowerIDTO dto = new FollowerIDTO();
        dto.setFollowerId(userId);
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", inexistentUserId)
                .when().put()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when follower does not exist")
    void followerNotFoundTest() {
        FollowerIDTO dto = new FollowerIDTO();
        dto.setFollowerId(inexistentUserId);
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", userId)
                .when().put()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should follow the user")
    void followUserTest() {
        FollowerIDTO dto = new FollowerIDTO();
        dto.setFollowerId(followerId);
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", userId)
                .when().put()
                .then().statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 on list user followers and user does not exist")
    void listFollowersUserNotFoundTest() {
        given().pathParam("userId", inexistentUserId)
                .when().get()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should return 200 with the followers list")
    void listFollowersTest() {
        Response response = given().pathParam("userId", userId)
                .when().get()
                .then().extract().response();
        Integer followersCount = response.jsonPath().get("followersCount");
        List<FollowerODTO> followers = response.jsonPath().getList("followers");
        assertEquals(Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followers.size());
    }

    @Test
    @DisplayName("Should return 404 on unfollow an user when user does not exist")
    void userNotFoundWhenUnfollowAnUserTest() {
        given().pathParam("userId", inexistentUserId).queryParam("followerId", followerId)
                .when().delete()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 on unfollow an user when follower does not exist")
    void followerNotFoundWhenUnfollowAnUserTest() {
        given().pathParam("userId", userId).queryParam("followerId", inexistentUserId)
                .when().delete()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should unfollow an user")
    void unfollowerUserTest() {
        given().pathParam("userId", userId).queryParam("followerId", followerId)
                .when().delete()
                .then().statusCode(Status.NO_CONTENT.getStatusCode());
    }
}