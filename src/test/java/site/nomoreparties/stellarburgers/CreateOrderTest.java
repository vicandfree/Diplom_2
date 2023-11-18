package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import site.nomoreparties.stellarburgers.clients.*;
import site.nomoreparties.stellarburgers.models.*;
import org.junit.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private String accessToken;

    private static final String INGREDIENT_IDS_MUST_BE_PROVIDED = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        User user = User.withAllFields();

        accessToken = userClient.registerUser(user)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");
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
    @DisplayName("Создание заказа с верными ингредиентами")
    public void createOrderTest() {
        Order fullOrderRequest = Order.withValidIngredients();
        orderClient.createNewOrderWithAuth(fullOrderRequest, accessToken)
                .then().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с верными ингредиентами, но без авторизации")
    public void createOrderUnauthorizedTest() {
        orderClient.createNewOrderWithoutAuth(Order.withValidIngredients())
                .then().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order emptyOrderRequest = Order.withoutIngredients();
        orderClient.createNewOrderWithAuth(emptyOrderRequest, accessToken)
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo(INGREDIENT_IDS_MUST_BE_PROVIDED));
    }

    @Test
    @DisplayName("Создание заказа с неверными ингредиентами")
    public void createOrderWithInvalidIngredirentsTest() {
        Order wrongOrderRequest = Order.withWrongIngredients();
        orderClient.createNewOrderWithAuth(wrongOrderRequest, accessToken)
                .then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}