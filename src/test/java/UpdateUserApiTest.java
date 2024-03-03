import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;


public class UpdateUserApiTest {
    int randomNumber = new Random().nextInt(1000000);
    String email = "garri-" + randomNumber + "@yandex.ru";
    String password = "1234";
    String name = "garri-" + randomNumber;
    RegistrationUserApi registrationUserApi = new RegistrationUserApi();
    AuthorizationUserApi authorizationUserApi = new AuthorizationUserApi();
    UpdateUserApi updateUserApi = new UpdateUserApi();
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
    @DisplayName("updateUserTest") // имя теста
    @Description("Редактирование клиента с регистрацией. Ответ 200") // описание теста
    public void updateUserWithRegistrationTest() {
        Response response = updateUserApi.updateUserWithRegistration(email, password, name);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("updateUserTest2") // имя теста
    @Description("Редактирование клиента с регистрацией, но почта уже используется. Ответ 403") // описание теста
    public void updateUserWithRegistrationTest2() {
        registrationUserApi.registrationUser("new" + email, password, name); //Создание дополнительного клиента
        Response response = updateUserApi.updateUserWithRegistration(email, password, name);
        response.then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("updateUserWithoutRegistrationTest") // имя теста
    @Description("Редактирование клиента без регистрацией. Ответ 200") // описание теста
    public void updateUserWithoutRegistrationTest() {
        Response response = updateUserApi.updateUserWithoutRegistration(email, password, name);
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
        String token2 = new AuthorizationUserApi().getAccessToken("new" + email, "new" + password, "new" + name);
        System.out.println("Получен токен для удаления клиента:" + token);
        new DeleteUserApi().deleteUser(token2);
    }


}