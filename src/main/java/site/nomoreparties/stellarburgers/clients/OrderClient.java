package site.nomoreparties.stellarburgers.clients;

import io.qameta.allure.Step;

import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.models.Order;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.config.Configuration.*;

public class OrderClient {

    @Step
    public Response createNewOrderWithAuth(Order order, String accessToken) {
        Response response = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(order)
                .when()
                .post(ORDERS);

        response.then();
        return  response;
    }

    @Step
    public Response createNewOrderWithoutAuth(Order order) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(order)
                .when()
                .post(ORDERS);
        response.then();
        return  response;
    }

    @Step
    public Response getOrderListForAuthUser(String accessToken) {
        Response response = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .get("orders");
        response.then();
        return response;
    }

    @Step
    public Response getOrderListWithoutAuth() {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .get("orders");
        response.then();
        return response;
    }
}