import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class Utility {

    public static String createUserAndGetAccessToken(User user) {
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

    public static int createOrderAndGetOrderNumber(String accessToken, Order order) {
        Gson gson = new Gson();

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(Utility.cleanAccessToken(accessToken))
                        .and()
                        .body(gson.toJson(order))
                        .when()
                        .post(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().body("name", notNullValue())
                .and().statusCode(200);

        return response.then().extract().path("order.number");
    }
}
