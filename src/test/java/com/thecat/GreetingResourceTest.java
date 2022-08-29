package com.thecat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testGreetingEndpoint() {
        given()
          .when().get("/greeting")
          .then()
             .statusCode(200)
             .body(is("Welcome to your first Quarkus app!"));
    }

    @Test
    public void testRhugEndpoint() {
        given()
          .when().get("/greeting/rhug")
          .then()
             .statusCode(200)
             .body(is("Bienvenue au RHUG de Québec et Montréal") );
    }

    @Test
    public void testServingEndpoint() {
        given()
          .when().get("/greeting/serverless")
          .then()
             .statusCode(200)
             .body(is("Welcome to the app in Knative serving!") );
    }

}