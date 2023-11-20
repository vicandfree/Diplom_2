package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import site.nomoreparties.stellarburgers.clients.UserClient;
import site.nomoreparties.stellarburgers.models.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {

    private UserClient userClient;
    private String accessToken;

    private static final String USER_ALREADY_EXISTS = "User already exists";
    private static final String EMAIL_PASSWORD_AND_NAME_ARE_REQUIRED_FIELDS = "Email, password and name are required fields";

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
    @DisplayName("Создание пользователя")
    public void newUserRegisterTest() {
        User userForCorrectReg = User.withAllFields();
        userClient.registerUser(userForCorrectReg)
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));

        accessToken = userClient.getAccessToken(userForCorrectReg);
    }

    @Test
    @DisplayName("Попытка создать уже зарегистрированного пользователя")
    public void existingUserRegisterTest() {
        User doubleUserForReg = User.withAllFields();
        userClient.registerUser(doubleUserForReg);
        userClient.registerUser(doubleUserForReg)
                .then().statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("message", equalTo(USER_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("Попытка создать пользователя с не заполненным обязательным полем email")
    public void userWithoutEmailRegisterTest() {
        userClient.registerUser(User.withoutMail())
                .then().statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("message", equalTo(EMAIL_PASSWORD_AND_NAME_ARE_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Попытка создать пользователя с не заполненным обязательным полем password")
    public void userWithoutPasswordRegisterTest() {
        userClient.registerUser(User.withoutPassword())
                .then().statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("message", equalTo(EMAIL_PASSWORD_AND_NAME_ARE_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Попытка создать пользователя с не заполненным обязательным полем name")
    public void userWithoutNameRegisterTest() {
        User.withAllFields();
        userClient.registerUser(User.withoutName())
                .then().statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("message", equalTo(EMAIL_PASSWORD_AND_NAME_ARE_REQUIRED_FIELDS));
    }
}
