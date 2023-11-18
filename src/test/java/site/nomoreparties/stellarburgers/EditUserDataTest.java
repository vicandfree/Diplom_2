package site.nomoreparties.stellarburgers;


import io.qameta.allure.junit4.DisplayName;

import org.junit.*;
import site.nomoreparties.stellarburgers.clients.UserClient;
import site.nomoreparties.stellarburgers.models.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@DisplayName("Изменение данных пользователя")
public class EditUserDataTest {

    private UserClient userClient;
    private String accessToken;

    private static final String YOU_SHOULD_BE_AUTHORISED = "You should be authorised";

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void deleteTestUser() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken)
                    .then()
                    .statusCode(202);
        }
    }

    @Test
    @DisplayName("Изменение данных авторизованным пользователем")
    public void editUserDataTest() {
        User user = User.withAllFields();
        User changedUser = User.editData();

        userClient.registerUser(user);
        accessToken = userClient.getAccessToken(user);

        userClient.editUserData(changedUser, accessToken)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.email", is(changedUser.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Изменение данных неавторизованным пользователем")
    public void editUserDataUnauthorizedTest() {
        User user = User.withAllFields();
        userClient.editUserData(user)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo(YOU_SHOULD_BE_AUTHORISED));
    }
}
