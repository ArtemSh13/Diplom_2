import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    private Order order;
    private String orderJson;
    private final Gson gson = new Gson();
    private int orderNumber;

    @Before
    public void setUp() {
        RestAssured.baseURI = StellarBurgersAPI.BASE_URL;

        order = new Order(new ArrayList<>());
        // Флюоресцентная булка R2-D3
        order.addIngredient("61c0c5a71d1f82001bdaaa6d");
        // Мясо бессмертных моллюсков Protostomia
        order.addIngredient("61c0c5a71d1f82001bdaaa6f");
        orderJson = gson.toJson(order);
    }

    // с авторизацией, с ингредиентами
    @Test
    public void createOrderWithAuthAndCheckResponse() {
        User user = new User(DataGenerator.getRandomEmail(), DataGenerator.getRandomPassword(), DataGenerator.getRandomName());
        String accessToken = Utility.createUserAndGetAccessToken(user);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(Utility.cleanAccessToken(accessToken))
                        .and()
                        .body(orderJson)
                        .when()
                        .post(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().body("name", notNullValue())
                .and().statusCode(200);

        orderNumber = response.then().extract().path("order.number");

        Utility.deleteUser(accessToken);
    }

    // без авторизации, с ингредиентами
    @Test
    public void createOrderWithoutAuthAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderJson)
                        .when()
                        .post(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().body("name", notNullValue())
                .and().statusCode(200);

        orderNumber = response.then().extract().path("order.number");

    }

    // без ингредиентов
    @Test
    public void createOrderWithoutAuthAndIngredientsCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .post(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo(StellarBurgersAPI.expectedRequiredIngredientsMessage))
                .and().statusCode(400);

    }

    // с неверным хешем ингредиентов
    @Test
    public void createOrderWithoutAuthAndWithInvalidIngredientsAndCheckResponse() {
        order.addIngredient("qwerty1234567890");
        orderJson = gson.toJson(order);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderJson)
                        .when()
                        .post(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().statusCode(500);

    }
}
