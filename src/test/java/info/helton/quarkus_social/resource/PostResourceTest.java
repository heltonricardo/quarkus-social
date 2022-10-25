package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import info.helton.quarkus_social.dto.input.PostIDTO;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;
    Long followerId;
    Long inexistentUserId = -1L;

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setName("José");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();
        User follower = new User();
        follower.setName("Maria");
        follower.setAge(20);
        userRepository.persist(follower);
        followerId = follower.getId();
    }

    @BeforeEach
    @Transactional
    void deleteUsers() {
        userRepository.deleteAll();
    }

    void follow() {

    }

    @Test
    @DisplayName("Should create a post for an user")
    void createPostTest() {
        PostIDTO dto = new PostIDTO();
        dto.setText("Postagem");
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", userId)
                .when().post()
                .then().statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    @DisplayName("Should return error when trying to make a post for an inexistent user")
    void postForAnInexistentUserTest() {
        PostIDTO dto = new PostIDTO();
        dto.setText("Postagem");
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", inexistentUserId)
                .when().post()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    public void listAllFollowerIdHeaderNotSendTest() {
        given().contentType(ContentType.JSON).pathParam("userId", inexistentUserId)
                .when().get()
                .then().statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when user does not exist")
    public void listAllUserNotFoundTest() {
        Map<String, Long> header = new HashMap<>();
        header.put("followerId", followerId);
        Map<String, Long> path = new HashMap<>();
        path.put("userId", inexistentUserId);
        given().contentType(ContentType.JSON).headers(header).pathParams(path)
                .when().get()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when follower does not exist")
    public void listAllFollowerNotFoundTest() {
        Map<String, Long> header = new HashMap<>();
        header.put("followerId", inexistentUserId);
        Map<String, Long> path = new HashMap<>();
        path.put("userId", userId);
        given().contentType(ContentType.JSON).headers(header).pathParams(path)
                .when().get()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("Should return 403 when follower does not follow user")
    public void listAllFollowerNotFollowsUserFoundTest() {
        Map<String, Long> header = new HashMap<>();
        header.put("followerId", followerId);
        Map<String, Long> path = new HashMap<>();
        path.put("userId", userId);
        given().contentType(ContentType.JSON).headers(header).pathParams(path)
                .when().get()
                .then().statusCode(Status.FORBIDDEN.getStatusCode());

    }
}