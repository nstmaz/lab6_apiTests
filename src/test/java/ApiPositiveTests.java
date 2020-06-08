import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volsu.api.EndPoints;
import com.volsu.api.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
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
                .get(EndPoints.users)
                .then()
                .assertThat()
                .body("_meta.code", equalTo(200))
                .log().all();
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
                .body("result.first_name", hasItem(firstName))
                .log().body();
    }

    @DataProvider(name = "ValidUser")
    public Object[][] ValidUserDP() {
        User usr = new User("Hayley", "Smith", "female");
        return new Object[][]{
                {usr}
        };
    }

    // 3. POST /public-api/users : создать нового пользователя
    @Test(dataProvider = "UsersWithBaseAndRequiredFields")
    public void testCreateUser(User usr) throws JsonProcessingException {
        requestSpec
                .body(objectMapper.writeValueAsString(usr))
                .when()
                .post(EndPoints.users)
                .then()
                .assertThat()
                .body("_meta.code", equalTo(201))
                .body("result.email", equalTo(usr.getEmail()))
                .body("result.first_name", equalTo(usr.getFirst_name()))
                .body("result.last_name", equalTo(usr.getLast_name()))
                .body("result.gender", equalTo(usr.getGender()))
                .log().body();
    }

    @DataProvider(name = "UsersWithBaseAndRequiredFields")
    public Object[][] UserDP() {
        return new Object[][]{
                // user with base fields
                {new User("Hayley", "Smith", "female")},
                // user with base and some optional fileds
                {new User("Sam", "Black", "male", "27456 Emerald Pines Apt. 623", "1982-09-23")},
        };
    }

    // 4. GET /public-api/users/<user_id> : получить пользователя по его ID
    @Test(dataProvider = "UserID")
    public void testGetUserWithID(int id) {

        //int Id = getUserIdFromTestCreateUser("wll91zxgocbxj@mail.example");

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

        User usr = new User("AAA", "bbb", "male","LA","1982-09-23");

        requestSpec
                .body(objectMapper.writeValueAsString(usr))
                .when()
                .put(EndPoints.usersWithID(id))
                .then()
                .assertThat()
                .body("_meta.code", equalTo(200))
                .body("result.id", equalTo(Integer.toString(id)))
                .body("result.email", equalTo(usr.getEmail()))
                .body("result.first_name", equalTo(usr.getFirst_name()))
                .body("result.last_name", equalTo(usr.getLast_name()))
                .body("result.gender", equalTo(usr.getGender()))
                .body("result.address", equalTo(usr.getAddress()))
                .body("result.dob", equalTo(usr.getDob()))
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
                {31319}
        };
    }

//    public int getUserIdFromTestCreateUser(String email) {
//        String json =
//                given()
//                        .contentType(ContentType.JSON)
//                        .accept(ContentType.JSON)
//                        .header("Authorization",
//                                "Bearer " + EndPoints.token)
//                        .when()
//                        .get(EndPoints.usersWithEmail(email)).asString();
//
//        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
//        JSONObject obj = new JSONObject(json);
//        String pageName = obj.getJSONObject("result").getString("email");
//
//        JsonArray arr = jsonObject.getAsJsonArray("result");
//        String id = arr.get(0).getAsJsonObject().get("id").getAsString();
//
//        return Integer.parseInt(id);
//    }
}
