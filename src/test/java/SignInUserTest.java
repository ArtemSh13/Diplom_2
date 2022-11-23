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
        accessToken = Utility.createUserAndGetaAccessToken(user);

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

    @Test
    public void signInUserWithInvalidEmail() {
        user.setEmail(DataGenerator.getRandomEmail());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @Test
    public void signInUserWithInvalidPassword() {
        user.setPassword(DataGenerator.getRandomPassword());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @Test
    public void signInUserWithInvalidName() {
        String oldUserName = user.getName();
        user.setName(DataGenerator.getRandomName());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userJson)
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(oldUserName))
                .and().statusCode(200);
    }

    @Test
    public void signInUserWithoutEmail() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user.getJsonWithoutEmail())
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @Test
    public void signInUserWithoutPassword() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user.getJsonWithoutPassword())
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @Test
    public void signInUserWithoutName() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user.getJsonWithoutName())
                        .when()
                        .post(StellarBurgersAPI.SIGN_IN_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()))
                .and().statusCode(200);
    }

    @After
    public void teardown() {
        Utility.deleteUser(accessToken);
    }

}
