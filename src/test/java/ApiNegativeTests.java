import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volsu.api.EndPoints;
import com.volsu.api.ErrorMsg;
import com.volsu.api.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class ApiNegativeTests {

    RequestSpecification requestSpec;
    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = "https://gorest.co.in";
        RestAssured.urlEncodingEnabled = false;

        // спецификация для запросов
        requestSpec = given()
                .contentType(ContentType.JSON)
                .header(
                        "Authorization",
                        "Bearer " + EndPoints.token);
    }

    // 1. GET /public-api/users
    // без указания токена авторизации и с некорректным токеном
    @Test
    public void testNoToken() {
        when().
                get(EndPoints.users).
                then()
                .assertThat()
                .log().all().
                body("_meta.code", equalTo(401)).
                body("_meta.message", equalTo(ErrorMsg.getErr401Msg()));
    }

    // 1. GET /public-api/users
    // с некорректным токеном
    @Test
    public void testWrongToken() {
        given().
                contentType(ContentType.JSON)
                .header("Authorization",
                        "Bearer " + EndPoints.token + "abcd").
                when().
                get(EndPoints.users).
                then()
                .assertThat()
                .log().all().
                body("_meta.code", equalTo(401)).
                body("_meta.message", equalTo(ErrorMsg.getErr401Msg()));
    }

    // POST /public-api/users с некорректным форматом тела запроса
    @Test
    public void testPostInvalidUser() throws JsonProcessingException {

        User usr = new User();

        given()
                .spec(requestSpec)
                .body(objectMapper.writeValueAsString(usr))
                .when()
                .post(EndPoints.users)
                .then()
                .assertThat()
                .body("_meta.code", equalTo(422))
                .body("_meta.message", equalTo(ErrorMsg.getErr422Msg()))
                .log().all();
    }

    // DELETE /public-api/users
    @Test
    public void testDeleteInvalidUser() {

        int id = 0;
        given()
                .spec(requestSpec)
                .when()
                .delete(EndPoints.usersWithID(id))
                .then()
                .assertThat()
                .body("_meta.code", equalTo(404))
                .body("_meta.message", equalTo(ErrorMsg.getErr404Msg()))
                .log().all();
    }
}
