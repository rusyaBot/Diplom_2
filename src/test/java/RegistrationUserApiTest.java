import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegistrationUserApiTest {
    int randomNumber = new Random().nextInt(1000000);
    String email = "garri-" + randomNumber + "@yandex.ru";
    String password = "1234";
    String name = "garri-" + randomNumber;
    RegistrationUserApi registrationUserApi = new RegistrationUserApi();
    BaseUrl baseUrl = new BaseUrl();

    @Before
    public void setUp() {
        baseUrl.getBaseUrl();
        String token = new AuthorizationUserApi().getAccessToken(email, password, name);
        System.out.println(token);
        new DeleteUserApi().deleteUser(token);
    }

    @Test // Создание клиента. Ответ 201
    @DisplayName("createNewUser") // имя теста
    @Description("Создание клиента. Ответ 201") // описание теста
    public void createNewUserTest() {
        Response response = registrationUserApi.registrationUser(email, password, name);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test // Создание клиента нет email. Ответ 403
    @DisplayName("createNewUserNoEmail") // имя теста
    @Description("Создание клиента нет email. Ответ 403") // описание теста
    public void createNewUserNoEmailTest() {
        Response response = registrationUserApi.registrationUser("", password, name);
        response.then().assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test // Создание клиента нет password. Ответ 403
    @DisplayName("createNewUserNoPassword") // имя теста
    @Description("Создание клиента нет пароля. Ответ 403") // описание теста
    public void createNewUserNoPasswordTest() {
        Response response = registrationUserApi.registrationUser(email, "", name);
        response.then().assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test // Создание клиента нет password. Ответ 403
    @DisplayName("createNewUserNoName") // имя теста
    @Description("Создание клиента нет имя. Ответ 403") // описание теста
    public void createNewUserNoNameTest() {
        Response response = registrationUserApi.registrationUser(email, password, "");
        response.then().assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test // Создание клиента нет password. Ответ 403
    @DisplayName("createNewUserNotFilled") // имя теста
    @Description("Создание клиента, не заполнены все данные. Ответ 403") // описание теста
    public void createNewUserNotFilledTest() {
        Response response = registrationUserApi.registrationUser("", "", "");
        response.then().assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test // Создание клиента, который уже существует. Ответ 403
    @DisplayName("createNewUserInUse") // имя теста
    @Description("Создание клиента логин уже используется. Ответ 403") // описание теста
    public void createNewUserInUseTest() {
        // Step1 Создание клиента1
        registrationUserApi.registrationUser(email, password, name);
        // Step2 Создание клиента2 с теми же данными
        Response response = registrationUserApi.registrationUser(email, password, name);
        response.then().assertThat()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @After // Удаление клиента
    public void deleteUser() {
        System.out.println("----------------Постусловие----------------");
        String token = new AuthorizationUserApi().getAccessToken(email, password, name);
        System.out.println("Получен токен для удаления клиента:" + token);
        new DeleteUserApi().deleteUser(token);
    }

}