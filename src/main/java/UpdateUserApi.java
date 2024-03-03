import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UpdateUserApi {
    private final String apiPath = "/api/auth/user";
    @Step("------Редактирование клиента с авторизации-----")
    public Response updateUserWithRegistration(String email, String password, String name) {
        System.out.println("------Редактирование клиента-----");
        String accessToken = new AuthorizationUserApi().getAccessToken(email, password, name);
        System.out.println(accessToken);
        User user = new User("new" + email, "new" + password, "new" + name);
        Response response =
                given()
                        .header("Authorization", accessToken)
                        .contentType("application/json")
                        .body(user)
                        .when()
                        .patch(apiPath)
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

    @Step("------Редактирование клиента без авторизации-----")
    public Response updateUserWithoutRegistration(String email, String password, String name) {
        System.out.println("------Редактирование клиента без авторизации-----");
        String accessToken = new AuthorizationUserApi().getAccessToken(email, password, name);
        System.out.println(accessToken);
        User user = new User("new" + email, "new" + password, "new" + name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .patch(apiPath)
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
