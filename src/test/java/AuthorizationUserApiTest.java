import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;


public class AuthorizationUserApiTest {

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
        registrationUserApi.registrationUser(email, password, name); //Создание нового клиента
        System.out.println("---------Клиент с email:" + email + " создан--------");
    }

    @Test
    @DisplayName("authorizationUser") // имя теста
    @Description("Авторизация клиента. Ответ 200") // описание теста
    public void authorizationUserTest() {
        Response response = authorizationUserApi.authorizationUser(email, password, name);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("authorizationUserNoEmail") // имя теста
    @Description("Авторизация клиента. Ответ 401") // описание теста
    public void authorizationUserNoEmailTest() {
        Response response = authorizationUserApi.authorizationUser("", password, name);
        response.then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("authorizationUserNoPassword") // имя теста
    @Description("Авторизация клиента. Ответ 401") // описание теста
    public void authorizationUserNoPasswordTest() {
        Response response = authorizationUserApi.authorizationUser(email, "", name);
        response.then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("authorizationUserNoName") // имя теста
    @Description("Авторизация клиента. Ответ 401") // описание теста
    public void authorizationUserNoNameTest() {
        Response response = authorizationUserApi.authorizationUser(email, password, "");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @After // Удаление клиента
    public void deleteUser() {
        System.out.println("----------------Постусловие----------------");
        String token = new AuthorizationUserApi().getAccessToken(email, password, name);
        System.out.println("Получен токен для удаления клиента:" + token);
        new DeleteUserApi().deleteUser(token);
    }

}