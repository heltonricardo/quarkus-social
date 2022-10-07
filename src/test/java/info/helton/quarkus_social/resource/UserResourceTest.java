package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import info.helton.quarkus_social.constant.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;
import info.helton.quarkus_social.error.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource(Route.USERS)
    URL url;

    @Test
    @Order(1)
    @DisplayName("Should create an user sucessfully")
    void createUserTest() {
        UserIDTO dto = new UserIDTO();
        dto.setName("João");
        dto.setAge(20);
        Response response = given().contentType(ContentType.JSON).body(dto)
                .when().post(url)
                .then().extract().response();
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @Order(2)
    @DisplayName("Should return error when json is not valid")
    void createUserValidationErrorTest() {
        UserIDTO dto = new UserIDTO();
        dto.setName("");
        dto.setAge(-1);
        Response response = given().contentType(ContentType.JSON).body(dto)
                .when().post(url)
                .then().extract().response();
        assertEquals(ResponseError.UNPROCESSABLE_ENTITY, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));
        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }

    @Test
    @Order(3)
    @DisplayName("Should list all users")
    void listAllUsersTest() {
        given().contentType(ContentType.JSON)
                .when().get(url)
                .then().statusCode(Status.OK.getStatusCode()).body("size()", Matchers.is(1));
    }
}
