package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import info.helton.quarkus_social.dto.input.PostIDTO;
import info.helton.quarkus_social.model.Follower;
import info.helton.quarkus_social.model.Post;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.FollowerRepository;
import info.helton.quarkus_social.repository.PostRepository;
import info.helton.quarkus_social.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
class PostResourceTest {

    final UserRepository userRepository;
    final FollowerRepository followerRepository;
    final PostRepository postRepository;

    Long userId;
    Long followerId;
    Long notAFollowerId;
    Long inexistentUserId = -1L;

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setName("José");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        Post post = new Post();
        post.setUser(user);
        post.setText("Hello!");
        postRepository.persist(post);
        
        User notAFollower = new User();
        notAFollower.setName("Maria");
        notAFollower.setAge(20);
        userRepository.persist(notAFollower);
        notAFollowerId = notAFollower.getId();
        
        User follower = new User();
        follower.setName("Julia");
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
        postRepository.deleteAll();
        followerRepository.deleteAll();
        userRepository.deleteAll();
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
        header.put("followerId", notAFollowerId);
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
        header.put("followerId", notAFollowerId);
        Map<String, Long> path = new HashMap<>();
        path.put("userId", userId);
        given().contentType(ContentType.JSON).headers(header).pathParams(path)
                .when().get()
                .then().statusCode(Status.FORBIDDEN.getStatusCode());

    }

    @Test
    @DisplayName("Should return 200 with posts")
    public void listAllTest() {
        Map<String, Long> header = new HashMap<>();
        header.put("followerId", followerId);
        Map<String, Long> path = new HashMap<>();
        path.put("userId", userId);
        given().contentType(ContentType.JSON).headers(header).pathParams(path)
                .when().get()
                .then().statusCode(Status.OK.getStatusCode());

    }
}