
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RegistrationUserApi {
    private final String apiPath = "/api/auth/register";
    @Step("------Регистрация клиент-----")
    public Response registrationUser(String email, String password, String name) {    // Создание клиента
        System.out.println("------Регистрация клиента-----");
        System.out.println("Запрос на создание клиента с email: " + email);
        System.out.println("password: " + password);
        System.out.println("name: " + name);
        User user = new User(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json") // заполнил header
                        .body(user) // заполнить body
                        .when()
                        .post(apiPath)// отправить запрос в ручку
                        .then().extract().response(); //вытащить ответ
        //логирование ответа
        System.out.println("Статус кода ответа: " + response.getStatusCode());
        System.out.println("Тело ответа: ");
        String[] lines = response.getBody().asString().split(","); //перенос строки если встретит запятую
        for (String line : lines) {
            System.out.println(line);
        }
        return response;
    }
}



