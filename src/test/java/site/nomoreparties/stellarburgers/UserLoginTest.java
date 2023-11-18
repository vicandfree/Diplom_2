package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import site.nomoreparties.stellarburgers.clients.UserClient;
import site.nomoreparties.stellarburgers.models.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {

    private UserClient userClient;
    private String accessToken;

    private static final String EMAIL_OR_PASSWORD_ARE_INCORRECT = "email or password are incorrect";

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @After
    public void deleteTestUser() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken)
                    .then()
                    .statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void authorizationTest() {
        User user = User.withAllFields();
        userClient.registerUser(user);
        accessToken = userClient.authorizeUser(user)
                .then().statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Авторизация пользователя с неверной почтой")
    public void authorizationWithInvalidEmailTest() {
        User user = User.withAllFields();
        userClient.registerUser(user);
        accessToken = userClient.getAccessToken(user);

        user.setEmail(User.createRandomEmail());

        userClient.authorizeUser(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo(EMAIL_OR_PASSWORD_ARE_INCORRECT));
    }
}