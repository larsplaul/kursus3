package test;

import org.junit.BeforeClass;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.parsing.Parser;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import static org.hamcrest.Matchers.*;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import test.utils.EmbeddedTomcat;

public class InitialSeedRestIntegrationTest {

  private static final int SERVER_PORT = 9999;
  private static final String APP_CONTEXT = "/seed";
  private static EmbeddedTomcat tomcat;

  public InitialSeedRestIntegrationTest() {
  }
  private String securityToken;

  //Utility method to login and set the securityToken
  private String login(String role, String password) {
    String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
    System.out.println(json);
    String token = given()
            .contentType("application/json")
            .body(json)
            .when().post("/api/login")
            .then()
            .extract().path("token");
    System.out.println("Token: " + token);
    return token;
  }

  private void logOut() {
    securityToken = null;
  }

  @BeforeClass
  public static void setUpBeforeAll() throws ServletException, MalformedURLException, LifecycleException {
    tomcat = new EmbeddedTomcat();
    tomcat.start(SERVER_PORT, APP_CONTEXT);
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = SERVER_PORT;
    RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;
    TestUsers.make();
  }

  @AfterClass
  public static void after() throws ServletException, MalformedURLException, LifecycleException, IOException {
    tomcat.stop();
  }

  @Test
  public void testRestNoAuthenticationRequired() {
    given()
            .contentType("application/json")
            .when()
            .get("/api/demoall").then()
            .statusCode(200)
            .body("message", equalTo("result for all"));
  }

  @Test
  public void tesRestForAdmin() {
    securityToken = login("admin", "test");
    given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + securityToken)
            .when()
            .get("/api/users").then()
            .statusCode(200)
            .body("users.size()",is(3));

    logOut();
  }

  @Test
  public void testRestForUser() {
    securityToken = login("user", "test");
    given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + securityToken)
            .when()
            .get("/api/demouser").then()
            .statusCode(200)
            .body("message", equalTo("Hello User from Server (Accesible by only authenticated USERS)"));
    logOut();
  }

  @Test
  public void userNotAuthenticated() {
    logOut();
    given()
            .contentType("application/json")
            .when()
            .get("/api/demouser").then()
            .statusCode(401)
            .body("error.message", equalTo("No authorization header provided"));
  }

  @Test
  public void adminNotAuthenticated() {
    logOut();
    given()
            .contentType("application/json")
            .when()
            .get("/api/users").then()
            .statusCode(401)
            .body("error.message", equalTo("No authorization header provided"));

  }

}
