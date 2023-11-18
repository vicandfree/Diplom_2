package site.nomoreparties.stellarburgers.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.models.User;


import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static site.nomoreparties.stellarburgers.config.Configuration.*;


public class UserClient {

    @Step("Запрос на регистрацию нового пользователя")
    public Response registerUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(user)
                .when()
                .post(REGISTER);

        response.then();
        return response;
    }

    @Step("Получение access token успешно зарегистрированного пользователя")
    public String getAccessToken(User user) {
        return authorizeUser(user)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    @Step("Запрос на авторизацию пользователя")
    public Response authorizeUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(user)
                .when()
                .post(LOGIN);

        response.then();
        return response;
    }

    @Step("Запрос на изменение данных авторизованного пользователя")
    public Response editUserData(User user, String accessToken) {
        Response response = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(user)
                .when()
                .patch(USER);

        response.then();
        return response;
    }

    @Step("Запрос на изменение данных неавторизованного пользователя")
    public Response editUserData(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(user)
                .when()
                .patch(USER);

        response.then();
        return response;
    }

    @Step("Запрос на удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .when()
                .delete(USER);
    }
}