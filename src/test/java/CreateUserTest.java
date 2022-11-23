import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    private User user;
    private String userJson;
    private final Gson gson = new Gson();
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = StellarBurgersAPI.BASE_URL;
        user = new User(DataGenerator.getRandomEmail(), DataGenerator.getRandomPassword(), DataGenerator.getRandomName());
        userJson = gson.toJson(user);
    }

    // создать уникального пользователя
    @Test
    public void createUniqueUserAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.CREATE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);

        accessToken = response.then().extract().path("accessToken");
        refreshToken = response.then().extract().path("refreshToken");
    }

    // создать пользователя, который уже зарегистрирован
    @Test
    public void createAlreadySignedUpUserAndCheckResponse() {
        Response successResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.CREATE_USER_API);

        successResponse.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);

        accessToken = successResponse.then().extract().path("accessToken");
        refreshToken = successResponse.then().extract().path("refreshToken");

        Response failedResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.CREATE_USER_API);

        failedResponse.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo(StellarBurgersAPI.expectedUserAlreadyExistsMessage))
                .and().statusCode(403);

    }

    @After
    public void teardown() {
        Response response =
                given()
                        .auth().oauth2(accessToken.replace("Bearer ", ""))
                        .when()
                        .delete(StellarBurgersAPI.UPDATE_AND_DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("message", equalTo(StellarBurgersAPI.expectedSuccessfulRemovedMessage))
                .and().statusCode(202);
    }
}
