package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;

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

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setName("José");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();
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
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", 0)
                .when().post()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }
}
