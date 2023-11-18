package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.*;
import site.nomoreparties.stellarburgers.models.*;

import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;


public class GetOrderListTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    private static final String YOU_SHOULD_BE_AUTHORISED = "You should be authorised";

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
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
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void getOrdersListTest() {
        User user = User.withAllFields();
        Order order = Order.withValidIngredients();

        userClient.registerUser(user);
        accessToken = userClient.getAccessToken(user);

        orderClient.createNewOrderWithAuth(order, accessToken);
        orderClient.getOrderListForAuthUser(accessToken)
                .then()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void getOrdersListUnauthorizedTest() {
        Order order = Order.withValidIngredients();
        orderClient.createNewOrderWithoutAuth(order);
        orderClient.getOrderListWithoutAuth()
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo(YOU_SHOULD_BE_AUTHORISED));
    }
}