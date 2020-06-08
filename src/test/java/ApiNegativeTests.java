import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volsu.api.EndPoints;
import com.volsu.api.ErrorMsg;
import com.volsu.api.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

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

    // 1.1 GET /public-api/users
    // без указания токена авторизации
    @Test
    public void testNoToken() {
        when()
                .get(EndPoints.users)
                .then()
                .assertThat()
                .log().all()
                .body("_meta.code", equalTo(401))
                .body("_meta.message", equalTo(ErrorMsg.getErr401Msg()));
    }

    // 1.2 GET /public-api/users
    // с некорректным токеном
    @Test
    public void testInvalidToken() {
        Random rnd = new Random();
        int invalidTokenLength = rnd.nextInt(EndPoints.token.length());

        given()
                .contentType(ContentType.JSON)

                // generate random string instead of valid token
                // its length is between 0 and real token length
                .header("Authorization",
                        "Bearer " + User.generateRandomString(invalidTokenLength))
                .when()
                .get(EndPoints.users)
                .then()
                .assertThat()
                .log().all()
                .body("_meta.code", equalTo(401))
                .body("_meta.message", equalTo(ErrorMsg.getErr401Msg()));
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
    public void testDeleteUser() {

        given()
                .spec(requestSpec)
                .when()
                .delete(EndPoints.users)
                .then()
                .assertThat()
                .body("_meta.code", equalTo(405))
                .body("_meta.message", equalTo(ErrorMsg.getErr405Msg()))
                .log().all();
    }
}
