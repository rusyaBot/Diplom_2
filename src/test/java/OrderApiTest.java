import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderApiTest {

    int randomNumber = new Random().nextInt(1000000);
    String email = "garri-" + randomNumber + "@yandex.ru";
    String password = "1234";
    String name = "garri-" + randomNumber;
    RegistrationUserApi registrationUserApi = new RegistrationUserApi();
    AuthorizationUserApi authorizationUserApi = new AuthorizationUserApi();
    BaseUrl baseUrl = new BaseUrl();


    @Before
    public void setUp() {
        System.out.println("---------Предусловие--------");
        baseUrl.getBaseUrl();
        String token = authorizationUserApi.getAccessToken(email, password, name); //Проверка, существует ли клиент и получение токена
        System.out.println(token);
        new DeleteUserApi().deleteUser(token); //Удаление клиента, необходимо для лучшей стабильности
        Response response = registrationUserApi.registrationUser(email, password, name); //Создание нового клиента
        System.out.println("---------Клиент с email:" + email + " создан--------");
    }

    @Test
    @DisplayName("newOrderWithRegistration") // имя теста
    @Description("Создание заказа с регистрацией. Правильными id ингредиентов. Ответ 200") // описание теста
    public void newOrderWithRegistration() {
        String token = authorizationUserApi.getAccessToken(email, password, name);
        OrderApi orderApi = new OrderApi();
        Response response = orderApi.newOrderWithRegistration(token, new Object[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("newOrderWithRegistration") // имя теста
    @Description("Создание заказа без регистрации. правильными id ингредиентов. Ответ 200") // описание теста
    public void newOrderWithoutRegistration() {
        OrderApi orderApi = new OrderApi();
        Response response = orderApi.newOrderWithoutRegistration(new Object[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("newOrderWithoutIngredients") // имя теста
    @Description("Создание заказа с регистрацией. Без ингредиентов. Ответ 400") // описание теста
    public void newOrderWithoutIngredients() {
        String token = authorizationUserApi.getAccessToken(email, password, name);
        OrderApi orderApi = new OrderApi();
        Response response = orderApi.newOrderWithRegistration(token, new Object[]{});
        response.then().assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("newOrderWithoutIngredients") // имя теста
    @Description("Создание заказа с регистрацией. Передан невалидный хеш ингредиента. Ответ 500") // описание теста
    public void newOrderBadIngredients() {
        String token = authorizationUserApi.getAccessToken(email, password, name);
        OrderApi orderApi = new OrderApi();
        Response response = orderApi.newOrderWithRegistration(token, new Object[]{"0909"});
        response.then().assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("checkingOrderWithRegistration") // имя теста
    @Description("Проверка заказов клиента с регистрацией. Ответ 200") // описание теста
    public void checkingOrderWithRegistration() {
        String token = authorizationUserApi.getAccessToken(email, password, name);
        OrderApi orderApi = new OrderApi();
        orderApi.newOrderWithRegistration(token, new Object[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});
        Response response = orderApi.getOrderWithRegistration(token);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders[0].name", equalTo("Бессмертный флюоресцентный бургер"));
    }

    @Test
    @DisplayName("checkingOrderWithoutRegistration") // имя теста
    @Description("Проверка заказов клиента без регистрацией. Ответ 401") // описание теста
    public void checkingOrderWithoutRegistration() {
        String token = authorizationUserApi.getAccessToken(email, password, name);
        OrderApi orderApi = new OrderApi();
        orderApi.newOrderWithRegistration(token, new Object[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});
        Response response = orderApi.getOrderWithoutRegistration();
        response.then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }


    @After // Удаление клиента
    public void deleteUser() {
        System.out.println("----------------Постусловие----------------");
        String token = new AuthorizationUserApi().getAccessToken(email, password, name);
        System.out.println("Получен токен для удаления клиента:" + token);
        new DeleteUserApi().deleteUser(token);
    }
}