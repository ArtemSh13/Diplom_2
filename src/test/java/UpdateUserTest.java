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

public class UpdateUserTest {
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

    @Test
    public void updateEmailOfSignedInUser() {
        user.setEmail("updated_" + user.getEmail());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(accessToken.replace("Bearer ", ""))
                        .body(userJson)
                        .when()
                        .patch(StellarBurgersAPI.UPDATE_AND_DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()))
                .and().statusCode(200);

    }

    @Test
    public void updateNameOfSignedInUser() {
        user.setName("updated_" + user.getName());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(accessToken.replace("Bearer ", ""))
                        .body(userJson)
                        .when()
                        .patch(StellarBurgersAPI.UPDATE_AND_DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()))
                .and().statusCode(200);

    }

    @Test
    public void updateEmailOfNotSignedInUser() {
        user.setEmail("updated_" + user.getEmail());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(userJson)
                        .when()
                        .patch(StellarBurgersAPI.UPDATE_AND_DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo(StellarBurgersAPI.expectedRequiredAuthorisedMessage))
                .and().statusCode(401);

    }

    @Test
    public void updateNameOfNotSignedInUser() {
        user.setName("updated_" + user.getName());
        userJson = gson.toJson(user);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(userJson)
                        .when()
                        .patch(StellarBurgersAPI.UPDATE_AND_DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo(StellarBurgersAPI.expectedRequiredAuthorisedMessage))
                .and().statusCode(401);

    }

    @After
    public void teardown() {
        Utility.deleteUser(accessToken);
    }
}
