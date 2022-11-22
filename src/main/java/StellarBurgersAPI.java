public class StellarBurgersAPI {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    public static final String CREATE_USER_API = "api/auth/register/";
    public static final String SIGN_IN_USER_API = "api/auth/login/";
    public static final String UPDATE_AND_DELETE_USER_API = "api/auth/user";
    public static final String CREATE_ORDER_API = "api/orders";
    public static final String expectedUserAlreadyExistsMessage = "User already exists";
    public static final String expectedRequiredFieldsMessage = "Email, password and name are required fields";
    public static final String expectedRequiredAuthorisedMessage = "You should be authorised";
    public static final String expectedSuccessfulRemovedMessage = "User successfully removed";

}
