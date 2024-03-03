import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteUserApi {
    private final String apiPath = "/api/auth/user";

    @Step("------Удаление клиента-----")
    public void deleteUser(String token) {
        System.out.println("------Удаление клиента-----");
        if (token != "Empty User") {
            Response response =
                    given()
                            .header("Authorization", token)
                            .when()
                            .delete(apiPath);// отправить запрос в ручку
            response.then().assertThat();
            System.out.println("Статус кода ответа: " + response.getStatusCode());
            System.out.println("Тело ответа: ");
            String[] lines = response.getBody().asString().split(","); //перенос строки если встретит запятую
            for (String line : lines) {
                System.out.println(line);

            }
            System.out.println("Клиент удалён");
        } else System.out.println("Нет клиента для удаления");
    }
}