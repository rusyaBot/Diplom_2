import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthorizationUserApi {
    private final String apiPath = "/api/auth/login";

    @Step("------Авторизация клиента-----")
    public Response authorizationUser(String email, String password, String name) {
        System.out.println("------Авторизация клиента-----");
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

    //Получение токена (accessToken)
    @Step("------Получение токена (accessToken)-----")
    public String getAccessToken(String email, String password, String name) {
        System.out.println("------Получение токена (accessToken)-----");
        Response response = authorizationUser(email, password, name); // Авторизация юзера
        if (response.statusCode() == 200) {
            String token = response.jsonPath().getString("accessToken");
            return token;
        }
        return "Empty User";
    }

    //Получение токена (refreshToken)
    public String getRefreshToken(String email, String password, String name) {    // Авторизация юзера
        System.out.println("------Получение токена (refreshToken)-----");
        Response response = authorizationUser(email, password, name); // Авторизация юзера
        if (response.statusCode() == 200) {
            String token = response.jsonPath().getString("refreshToken");
            return token;
        }
        return "Empty User";
    }


}



