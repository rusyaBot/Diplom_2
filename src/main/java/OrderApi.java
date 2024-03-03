import io.qameta.allure.Step;
import io.restassured.response.Response;

import static groovy.json.JsonOutput.toJson;
import static io.restassured.RestAssured.given;

public class OrderApi {

    private final String apiPath = "/api/orders";
    @Step("------Создание нового заказа клиента-----")
    public Response newOrderWithRegistration(String token, Object[] ingredients) {
        System.out.println("------Создание нового заказа клиента-----");
        Order order = new Order(ingredients);
        System.out.println("Отправляемый JSON: " + toJson(order));
        Response response =
                given()
                        .log().all().header("Authorization", token)
                        .contentType("application/json")
                        .body(order)
                        .when()
                        .post(apiPath)
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
    @Step("------Создание нового заказа без авторизации клиента-----")
    public Response newOrderWithoutRegistration(Object[] ingredients) {
        System.out.println("------Создание нового заказа без авторизации клиента-----");
        Order order = new Order(ingredients);
        System.out.println("Отправляемый JSON: " + toJson(order));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post(apiPath)
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
    @Step("------Получение всех заказов клиента с регистрацией-----")
    public Response getOrderWithRegistration(String token) {
        System.out.println("------Получение всех заказов клиента-----");
        Response response =
                given()
                        .header("Authorization", token)
                        .contentType("application/json")
                        .when()
                        .get(apiPath)
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
    @Step("------Получение всех заказов клиента без регистрации-----")
    public Response getOrderWithoutRegistration() {
        System.out.println("------Получение всех заказов клиента-----");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get(apiPath)
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
