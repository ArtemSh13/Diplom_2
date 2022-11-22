import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SignInUserTest {

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

    // логин под существующим пользователем
    @Test
    public void signInUserAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()))
                .and().statusCode(200);
    }

    @After
    public void teardown() {
        Response response =
                given()
                        .auth().oauth2(accessToken.replace("Bearer ", ""))
                        .when()
                        .delete(StellarBurgersAPI.DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("message", equalTo(StellarBurgersAPI.expectedSuccessfulRemovedMessage))
                .and().statusCode(202);
    }

}
