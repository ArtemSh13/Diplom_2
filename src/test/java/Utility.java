import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Utility {

    public static String createUserAndGetaAccessToken(User user) {
        Gson gson = new Gson();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(user))
                        .when()
                        .post(StellarBurgersAPI.CREATE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);

        return response.then().extract().path("accessToken");
    }

    public static String cleanAccessToken(String dirtyAccessToken) {
        return dirtyAccessToken.replace("Bearer ", "");
    }

    public static void deleteUser(String accessToken) {
        Response response =
                given()
                        .auth().oauth2(cleanAccessToken(accessToken))
                        .when()
                        .delete(StellarBurgersAPI.UPDATE_AND_DELETE_USER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("message", equalTo(StellarBurgersAPI.expectedSuccessfulRemovedMessage))
                .and().statusCode(202);
    }
}
