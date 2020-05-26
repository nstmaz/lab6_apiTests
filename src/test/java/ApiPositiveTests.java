import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volsu.api.EndPoints;
import com.volsu.api.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiPositiveTests {

    RequestSpecification requestSpec;
    ObjectMapper objectMapper = new ObjectMapper(); // объект для JSON сериализации

    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = "https://gorest.co.in";
        RestAssured.urlEncodingEnabled = false;

        // спецификация для всех запросов
        requestSpec = given()
                .contentType(ContentType.JSON)
                .header(
                        "Authorization",
                        "Bearer " + EndPoints.token);
        RestAssured.requestSpecification = requestSpec;
    }

    // 1. GET /public-api/users : получить список всех пользователей
    @Test
    public void testGetAllUsersList() {
        requestSpec
                .when()
                .get(EndPoints.users)
                .then()
                .assertThat()
                .body("_meta.code", equalTo(200))
                .log().body();
    }

    // 2. GET /public-api/users?first_name=<user_name> :
    // получить список пользователей с указанным именем
    @Test(dataProvider = "ValidUser")
    public void testGetUsersWithFirstName(User usr) {
        String firstName = usr.getFirst_name();
        requestSpec
                .get(EndPoints.usersWithFirstName(firstName))
                .then()
                .assertThat()
                .body("_meta.code", equalTo(200))
                .and()
                .body("result.first_name", hasItem(firstName))
                .log().body();
    }

    // 3. POST /public-api/users : создать нового пользователя
    @Test(dataProvider = "ValidUser")
    public void testCreateUser(User usr) throws JsonProcessingException {
        requestSpec
                .body(objectMapper.writeValueAsString(usr))
                .when()
                .post(EndPoints.users)
                .then()
                .assertThat()
                .body("_meta.code", equalTo(201))
                .and()
                .body("result.email", equalTo(usr.getEmail()))
                .body("result.first_name", equalTo(usr.getFirst_name()))
                .body("result.last_name", equalTo(usr.getLast_name()))
                .body("result.gender", equalTo(usr.getGender()))
                .log().body();
    }

    @DataProvider(name = "ValidUser")
    public Object[][] ValiduserDP() {
        User usr = new User("Hayley", "Smith", "female");
        return new Object[][]{
                {usr}
        };
    }

    // 4. GET /public-api/users/<user_id> : получить пользователя по его ID
    @Test(dataProvider = "UserID")
    public void testGetUserWithID(int id) {
        //id = getUsersIdFromTestCreateUser("ssfR@yvdssgho.com");
        requestSpec
                .when()
                .get(EndPoints.usersWithID(id))
                .then()
                .assertThat()
                .body("_meta.code", equalTo(200))
                .and()
                .body("result.id", equalTo(Integer.toString(id)))
                .log().body();
    }

    // 5. PUT /public-api/users/<user_id> : изменить пользователя с указанным ID
    @Test(dataProvider = "UserID")
    public void testPutUserWithID(int id) throws JsonProcessingException {

        User usr = new User("AAA", "bbb", "male","akfj@dfjn.dksfr");

        requestSpec
                .body(objectMapper.writeValueAsString(usr))
                .when()
                .put(EndPoints.usersWithID(id))
                .then()
                .assertThat()
                .body("_meta.code", equalTo(200))
                .and()
                .body("result.id", equalTo(Integer.toString(id)))
                .body("result.email", equalTo(usr.getEmail()))
                .body("result.first_name", equalTo(usr.getFirst_name()))
                .body("result.last_name", equalTo(usr.getLast_name()))
                .body("result.gender", equalTo(usr.getGender()))
                .log().body();
    }

    // 6. DELETE /public-api/users/<user_id> : удалить пользователя с указанным ID
    @Test(dataProvider = "UserID")
    public void testDeleteUserWithID(int id) {

        requestSpec
                .when()
                .delete(EndPoints.usersWithID(id))
                .then()
                .assertThat()
                .body("_meta.code", equalTo(204))
                .log().body();
    }

    @DataProvider(name = "UserID")
    public Object[][] userIDDP() {
        return new Object[][]{
                {27262}
        };
    }

//    public int getUserIdFromTestCreateUser(String email) {
//        User myUser = get(EndPoints.usersWithEmail(email)).as(User.class);
//        return myUser.getId();
//    }
}
