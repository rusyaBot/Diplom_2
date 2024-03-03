import io.restassured.RestAssured;

public class BaseUrl {
    private String baseUrl = "https://stellarburgers.nomoreparties.site/";

    public void getBaseUrl() {
        RestAssured.baseURI = baseUrl;
    }

}