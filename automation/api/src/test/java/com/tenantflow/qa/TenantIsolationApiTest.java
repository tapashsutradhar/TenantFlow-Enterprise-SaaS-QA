package com.tenantflow.qa;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TenantIsolationApiTest {
    static String baseUrl = System.getProperty("tenantflow.api", "http://localhost:8080");
    @BeforeAll static void configure() { io.restassured.RestAssured.baseURI = baseUrl; }

    @Test void northwindIdentityOnlyReceivesNorthwindMembers() {
        var response = given().auth().preemptive().basic("admin@northwind.test", "Northwind-Dev-Only!")
                .when().get("/api/members").then().statusCode(200).extract().response();
        response.then().body("email", not(empty())).body("email", org.hamcrest.Matchers.everyItem(not(equalTo("admin@contoso.test"))));
    }

    @Test void tenantCannotBeSelectedByAddingForeignTenantParameter() {
        given().auth().preemptive().basic("admin@northwind.test", "Northwind-Dev-Only!")
                .queryParam("tenantId", "d1a7d676-d651-46a2-9044-32a70b1e9ae3")
                .when().get("/api/members").then().statusCode(200)
                .body("email", not(org.hamcrest.Matchers.hasItem("admin@contoso.test")));
    }

    @Test void badCredentialsAreRejected() {
        given().auth().preemptive().basic("admin@northwind.test", "wrong")
                .when().get("/api/me").then().statusCode(401);
    }
}
