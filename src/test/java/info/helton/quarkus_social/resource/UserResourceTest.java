package info.helton.quarkus_social.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import info.helton.quarkus_social.constant.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;
import info.helton.quarkus_social.error.ResponseError;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("Should create an user sucessfully")
    void createUserTest() {
        UserIDTO dto = new UserIDTO();
        dto.setName("João");
        dto.setAge(20);
        Response response = given().contentType(ContentType.JSON).body(dto)
                .when().post(Route.USERS)
                .then().extract().response();
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    void createUserValidationErrorTest() {
        UserIDTO dto = new UserIDTO();
        dto.setName("");
        dto.setAge(-1);
        Response response = given().contentType(ContentType.JSON).body(dto)
                .when().post(Route.USERS)
                .then().extract().response();
        assertEquals(ResponseError.UNPROCESSABLE_ENTITY, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));
    }
}
