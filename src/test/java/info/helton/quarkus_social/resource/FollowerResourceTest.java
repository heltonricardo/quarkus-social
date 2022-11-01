package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import info.helton.quarkus_social.dto.input.FollowerIDTO;
import info.helton.quarkus_social.error.ResponseError;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
class PostResourceTest {

    final UserRepository userRepository;

    Long userId;
    Long inexistentUserId = -1L;

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setAge(30);
        user.setName("Ana");
        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    void followValidationErrorTest() {
        FollowerIDTO dto = new FollowerIDTO();
        dto.setFollowerId(inexistentUserId);
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
    @DisplayName("Should return 404 when userId does not exist")
    void userNotFoundTest() {
        FollowerIDTO dto = new FollowerIDTO();
        dto.setFollowerId(userId);
        given().contentType(ContentType.JSON).body(dto).pathParam("userId", inexistentUserId)
                .when().put()
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }
}