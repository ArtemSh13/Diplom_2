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

@RunWith(Parameterized.class)
public class CreateInvalidUserTest {
    private final String requestBody;

    public CreateInvalidUserTest(String requestBody) {
        this.requestBody = requestBody;
    }

    @Parameterized.Parameters
    public static Object[] getTestData() {
        User user = new User(DataGenerator.getRandomEmail(), DataGenerator.getRandomPassword(), DataGenerator.getRandomName());
        return new Object[] {
                user.getJsonWithoutEmail(),
                user.getJsonWithoutPassword(),
                user.getJsonWithoutName()
        };
    }

    // создать пользователя и не заполнить одно из обязательных полей
    @Test
    public void createUserWithoutRequiredFieldAndCheckResponse403() {
        RestAssured.baseURI = StellarBurgersAPI.BASE_URL;
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(requestBody)
                        .when()
                        .post(StellarBurgersAPI.CREATE_USER_API);

        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo(StellarBurgersAPI.expectedRequiredFieldsMessage))
                .and().statusCode(403);
    }
}
