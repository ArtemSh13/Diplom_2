import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderOfUserTest {

    private Order order;
    private final Gson gson = new Gson();
    private int orderNumber;

    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = StellarBurgersAPI.BASE_URL;

        order = new Order(new ArrayList<>());
        // Флюоресцентная булка R2-D3
        order.addIngredient("61c0c5a71d1f82001bdaaa6d");
        // Мясо бессмертных моллюсков Protostomia
        order.addIngredient("61c0c5a71d1f82001bdaaa6f");

        User user = new User(DataGenerator.getRandomEmail(), DataGenerator.getRandomPassword(), DataGenerator.getRandomName());
        accessToken = Utility.createUserAndGetAccessToken(user);
        orderNumber = Utility.createOrderAndGetOrderNumber(accessToken, order);
    }

    @Test
    public void getOrderByAuthUser() {
        Response response =
                given()
                        .and()
                        .auth().oauth2(Utility.cleanAccessToken(accessToken))
                        .when()
                        .get(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().body("success", equalTo(true))
                .and().body("orders", notNullValue())
                .and().body("total", notNullValue())
                .and().body("totalToday", notNullValue())
                .and().statusCode(200);
    }

    @Test
    public void getOrderByNotAuthUser() {
        Response response =
                given()
                        .when()
                        .get(StellarBurgersAPI.CREATE_AND_GET_ORDER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo(StellarBurgersAPI.expectedRequiredAuthorisedMessage))
                .and().statusCode(401);
    }

    @After
    public void teardown() {
        Utility.deleteUser(accessToken);
    }
}
